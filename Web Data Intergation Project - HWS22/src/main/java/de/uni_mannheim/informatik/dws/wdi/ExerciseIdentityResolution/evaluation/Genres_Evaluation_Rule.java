package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.lang.reflect.Array;
import java.util.Arrays;


public class Genres_Evaluation_Rule extends EvaluationRule<Song, Attribute> {
    @Override
    public boolean isEqual(Song record1, Song record2, Attribute schemaElement) {
        if(record1.getAlbum_genres().equals("-") && record2.getAlbum_genres().equals("-"))
            return true;
        else if(record1.getAlbum_genres().equals("-") ^ record2.getAlbum_genres().equals("-"))
            return false;
        else
            return ((Arrays.asList(record1.getAlbum_genres()).containsAll(Arrays.asList(record2.getAlbum_genres()))) || (Arrays.asList(record2.getAlbum_genres()).containsAll(Arrays.asList(record1.getAlbum_genres()))));
    }

    /* (non-Javadoc)
     * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
     */
    @Override
    public boolean isEqual(Song record1, Song record2,
                           Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return isEqual(record1, record2, (Attribute)null);
    }

}

