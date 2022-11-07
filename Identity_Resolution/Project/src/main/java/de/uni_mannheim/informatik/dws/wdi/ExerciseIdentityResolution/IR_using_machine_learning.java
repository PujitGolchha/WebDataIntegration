package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.SongBlockingKeyByTitleGenerator;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SongXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

import java.io.File;

public class IR_using_machine_learning {
	
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		logger.info("*\tLoading datasets\t*");
		HashedDataSet<Song, Attribute> dataDeezer = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/deezer.xml"), "/Songs/song", dataDeezer);
		HashedDataSet<Song, Attribute> dataMusico = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/musico_schema_new.xml"), "/Songs/song", dataMusico);
		HashedDataSet<Song, Attribute> dataSpotify = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/spotify.xml"), "/Songs/song", dataSpotify);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/ground_truth_train.csv"));

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Song, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000, gsTraining);
		
		// add comparators
//		matchingRule.addComparator(new SongArtistsComparatorGenJaccard());
//		matchingRule.addComparator(new SongArtistsComparatorGenMaxContainment());
		matchingRule.addComparator(new SongArtistsComparatorMaximumOfContainment());
		matchingRule.addComparator(new SongTrackComparatorLevenshteinEditDistance());
//		matchingRule.addComparator(new SongDateComparatorWeightedSimilarity());
//		matchingRule.addComparator(new AlbumTitleComparatorContainment());
		
		
		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		RuleLearner<Song, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataDeezer, dataMusico,null, matchingRule, gsTraining);
		learner.learnMatchingRule(dataDeezer, dataSpotify,null, matchingRule, gsTraining);
		learner.learnMatchingRule(dataSpotify, dataMusico,null, matchingRule, gsTraining);

		logger.info(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Song, Attribute> blocker = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByTitleGenerator());
//		StandardRecordBlocker<Song, Attribute> blocker = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByYearGenerator());
//		SortedNeighbourhoodBlocker<Movie, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new MovieBlockingKeyByDecadeGenerator(), 1);
		blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Song, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Song, Attribute>> correspondences1 = engine.runIdentityResolution(
				dataDeezer, dataMusico, null, matchingRule,
				blocker);
		Processable<Correspondence<Song, Attribute>> correspondences2 = engine.runIdentityResolution(
				dataDeezer, dataSpotify, null, matchingRule,
				blocker);
		Processable<Correspondence<Song, Attribute>> correspondences3 = engine.runIdentityResolution(
				dataSpotify, dataMusico, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_dez_musico.csv"), correspondences1);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_dez_spotify.csv"), correspondences2);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_spot_musico.csv"), correspondences3);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/ground_truth_test.csv"));
		
		// evaluate your result
		logger.info("*\tEvaluating result\t*");
		MatchingEvaluator<Song, Attribute> evaluator = new MatchingEvaluator<Song, Attribute>();
		Performance perfTest1 = evaluator.evaluateMatching(correspondences1,
				gsTest);
		Performance perfTest2 = evaluator.evaluateMatching(correspondences2,
				gsTest);
		Performance perfTest3 = evaluator.evaluateMatching(correspondences3,
				gsTest);
		
		// print the evaluation result
		logger.info("Deezer <-> Musico");
		logger.info(String.format(
				"Precision: %.4f",perfTest1.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest1.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest1.getF1()));

		logger.info("Deezer <-> Spotify");
		logger.info(String.format(
				"Precision: %.4f",perfTest2.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest2.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest2.getF1()));

		logger.info("Spotify <-> Musico");
		logger.info(String.format(
				"Precision: %.4f",perfTest3.getPrecision()));
		logger.info(String.format(
				"Recall: %.4f",	perfTest3.getRecall()));
		logger.info(String.format(
				"F1: %.4f",perfTest3.getF1()));
    }
}
