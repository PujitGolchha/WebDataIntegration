package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.evaluation;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


public class Genres_Evaluation_Rule extends EvaluationRule<Song, Attribute> {
    @Override
    public boolean isEqual(Song record1, Song record2, Attribute schemaElement) {
        if(record1.getAlbum_genres() == null && record2.getAlbum_genres()== null)
            return true;
        else if(record1.getAlbum_genres()== null ^ record2.getAlbum_genres()==null)
            return false;
        else
            return (record1.getAlbum_genres() == (record2.getAlbum_genres()));
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

