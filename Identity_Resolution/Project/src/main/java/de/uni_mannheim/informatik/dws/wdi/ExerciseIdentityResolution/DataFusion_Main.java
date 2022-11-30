package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SongXMLReader;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.SongXMLFormatter;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.slf4j.Logger;

public class DataFusion_Main 
{
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
		// Load the Data into FusibleDataSet
		logger.info("*\tLoading datasets\t*");


		FusibleDataSet<Song, Attribute> dataDeezer = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/deezer.xml"), "/Songs/song", dataDeezer);
		dataDeezer.printDataSetDensityReport();

		FusibleDataSet<Song, Attribute> dataMusico = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/musico_schema_new.xml"), "/Songs/song", dataMusico);
		dataMusico.printDataSetDensityReport();

		FusibleDataSet<Song, Attribute> dataSpotify = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/spotify.xml"), "/Songs/song", dataSpotify);
		dataSpotify.printDataSetDensityReport();


		// Maintain Provenance
		// Scores (e.g. from rating)
		dataDeezer.setScore(1.0);
		dataMusico.setScore(2.0);
		dataSpotify.setScore(3.0);

		// Date (e.g. last update)
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);

		dataDeezer.setDate(LocalDateTime.parse("2012-01-01", formatter));
		dataMusico.setDate(LocalDateTime.parse("2010-01-01", formatter));
		dataSpotify.setDate(LocalDateTime.parse("2008-01-01", formatter));

		// load correspondences
		logger.info("*\tLoading correspondences\t*");
		CorrespondenceSet<Song, Attribute> correspondences = new CorrespondenceSet<>();
		//not considering deezer-musico correspondences
//		correspondences.loadCorrespondences(new File("data/output/songs_correspondences_dez_musico.csv"),dataDeezer, dataMusico);
		correspondences.loadCorrespondences(new File("data/output/songs_correspondences_dez_spotify.csv"),dataSpotify,dataDeezer);
		correspondences.loadCorrespondences(new File("data/output/songs_correspondences_spot_musico.csv"),dataSpotify, dataMusico);


		// write group size distribution
		correspondences.printGroupSizeDistribution();

		// load the gold standard
		logger.info("*\tEvaluating results\t*");
		DataSet<Song, Attribute> gs = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/Songs/song", gs);

		for(Song m : gs.get()) {
			logger.info(String.format("gs: %s", m.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Song, Attribute> strategy = new DataFusionStrategy<>(new SongXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers
		strategy.addAttributeFuser(Song.Track_Name, new Track_Name_Fuser(), new Track_Name_Evaluation_Rule());
		strategy.addAttributeFuser(Song.Album_Name, new Album_Name_Fuser(),new Album_Name_Evaluation_Rule());
		strategy.addAttributeFuser(Song.Tempo, new Tempo_Fuser(),new Tempo_Evaluation_Rule());
		strategy.addAttributeFuser(Song.Release_Date, new Release_Date_Fuser(), new Release_Date_Evaluation_Rule());
		strategy.addAttributeFuser(Song.Artists, new Artists_Fuser(), new Artists_Evaluation_Rule());
		strategy.addAttributeFuser(Song.Album_Genres, new Genres_Fuser(), new Genres_Evaluation_Rule());
		
		// create the fusion engine
		DataFusionEngine<Song, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		logger.info("*\tRunning data fusion\t*");
		FusibleDataSet<Song, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new SongXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Song, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Song, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		logger.info(String.format("*\tAccuracy: %.2f", accuracy));
    }
}
