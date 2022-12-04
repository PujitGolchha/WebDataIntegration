package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Blocking.AlbumBlockingKeyByTitleGenerator;
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
		MatchingGoldStandard gsTraining1 = new MatchingGoldStandard();
		gsTraining1.loadFromCSVFile(new File("data/goldstandard/ground_truth_train_deezer_musico.csv"));

		MatchingGoldStandard gsTraining2 = new MatchingGoldStandard();
		gsTraining2.loadFromCSVFile(new File("data/goldstandard/ground_truth_train_deezer_spotify.csv"));

		MatchingGoldStandard gsTraining3 = new MatchingGoldStandard();
		gsTraining3.loadFromCSVFile(new File("data/goldstandard/ground_truth_train_musico_spotify.csv"));

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Song, Attribute> matchingRule1 = new WekaMatchingRule<>(0.5, modelType, options);
		matchingRule1.activateDebugReport("data/output/debugResultsMatchingRule1_ML.csv", 1000, gsTraining1);
		
		// add comparators
		matchingRule1.addComparator(new SongArtistsComparatorMaximumOfContainment());
		matchingRule1.addComparator(new SongTrackComparatorLevenshteinSim());
		matchingRule1.addComparator(new SongDateComparator2Years());
		matchingRule1.addComparator(new AlbumTitleComparatorLevenshtein());

		WekaMatchingRule<Song, Attribute> matchingRule2 = new WekaMatchingRule<>(0.5, modelType, options);
		matchingRule2.activateDebugReport("data/output/debugResultsMatchingRule2_ML.csv", 1000, gsTraining2);

		// add comparators
		matchingRule2.addComparator(new SongArtistsComparatorMaximumOfContainment());
		matchingRule2.addComparator(new SongTrackComparatorLevenshteinSim());
		matchingRule2.addComparator(new SongDateComparator2Years());
		matchingRule2.addComparator(new AlbumTitleComparatorLevenshtein());

		WekaMatchingRule<Song, Attribute> matchingRule3 = new WekaMatchingRule<>(0.5, modelType, options);
		matchingRule3.activateDebugReport("data/output/debugResultsMatchingRule3_ML.csv", 1000, gsTraining3);

		// add comparators
		matchingRule3.addComparator(new SongArtistsComparatorMaximumOfContainment());
		matchingRule3.addComparator(new SongTrackComparatorLevenshteinSim());
		matchingRule3.addComparator(new SongDateComparator2Years());
		matchingRule3.addComparator(new AlbumTitleComparatorLevenshtein());
		// train the matching rule's model
		logger.info("*\tLearning matching rule\t*");
		RuleLearner<Song, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dataDeezer, dataMusico,null, matchingRule1, gsTraining1);
		learner.learnMatchingRule(dataDeezer, dataSpotify,null, matchingRule2, gsTraining2);
		learner.learnMatchingRule(dataSpotify, dataMusico,null, matchingRule3, gsTraining3);

		logger.info(String.format("\nMatching rule 1 is:\n%s", matchingRule1.getModelDescription()));

		logger.info(String.format("\nMatching rule 2 is:\n%s", matchingRule2.getModelDescription()));

		logger.info(String.format("\nMatching rule 3 is:\n%s", matchingRule3.getModelDescription()));
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Song, Attribute> blocker1 = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByTitleGenerator());
		blocker1.collectBlockSizeData("data/output/debugResultsBlocking1_ML.csv", 100);

		StandardRecordBlocker<Song, Attribute> blocker2 = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByTitleGenerator());
		blocker2.collectBlockSizeData("data/output/debugResultsBlocking2_ML.csv", 100);

		StandardRecordBlocker<Song, Attribute> blocker3 = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByTitleGenerator());
		blocker3.collectBlockSizeData("data/output/debugResultsBlocking3_ML.csv", 100);
		// Initialize Matching Engine
		MatchingEngine<Song, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		logger.info("*\tRunning identity resolution\t*");
		Processable<Correspondence<Song, Attribute>> correspondences1 = engine.runIdentityResolution(
				dataDeezer, dataMusico, null, matchingRule1,
				blocker1);
		Processable<Correspondence<Song, Attribute>> correspondences2 = engine.runIdentityResolution(
				dataDeezer, dataSpotify, null, matchingRule2,
				blocker2);
		Processable<Correspondence<Song, Attribute>> correspondences3 = engine.runIdentityResolution(
				dataSpotify, dataMusico, null, matchingRule3,
				blocker3);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_dez_musico_ML.csv"), correspondences1);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_dez_spotify_ML.csv"), correspondences2);
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/songs_correspondences_spot_musico_ML.csv"), correspondences3);

		// load the gold standard (test set)
		logger.info("*\tLoading gold standard\t*");
		MatchingGoldStandard gsTest1 = new MatchingGoldStandard();
		gsTest1.loadFromCSVFile(new File(
				"data/goldstandard/ground_truth_test_deezer_musico.csv"));

		MatchingGoldStandard gsTest2 = new MatchingGoldStandard();
		gsTest2.loadFromCSVFile(new File(
				"data/goldstandard/ground_truth_test_deezer_spotify.csv"));

		MatchingGoldStandard gsTest3 = new MatchingGoldStandard();
		gsTest3.loadFromCSVFile(new File(
				"data/goldstandard/ground_truth_test_musico_spotify.csv"));
		
		// evaluate your result
		logger.info("*\tEvaluating result\t*");
		MatchingEvaluator<Song, Attribute> evaluator = new MatchingEvaluator<Song, Attribute>();
		Performance perfTest1 = evaluator.evaluateMatching(correspondences1,
				gsTest1);
		Performance perfTest2 = evaluator.evaluateMatching(correspondences2,
				gsTest2);
		Performance perfTest3 = evaluator.evaluateMatching(correspondences3,
				gsTest3);
		
		// print the evaluation result
		logger.info("Deezer <-> Musico");
		logger.info(String.format(
				"Precision, Recall, F1 "));
		logger.info(String.format(
				"%.4f %.4f %.4f",perfTest1.getPrecision(),perfTest1.getRecall(),perfTest1.getF1()));
		logger.info("\n");


		logger.info("Spotify <-> Deezer");
		logger.info(String.format(
				"Precision, Recall, F1 "));
		logger.info(String.format(
				"%.4f %.4f %.4f",perfTest2.getPrecision(),perfTest2.getRecall(),perfTest2.getF1()));


		logger.info("\n");

		logger.info("Spotify <-> Musico");
		logger.info(String.format(
				"Precision, Recall, F1 "));
		logger.info(String.format(
				"%.4f %.4f %.4f",perfTest3.getPrecision(),perfTest3.getRecall(),perfTest3.getF1()));
    }
}
