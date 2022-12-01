package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Intersection;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Union;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.Arrays;
import java.util.List;

public class Genres_Fuser extends AttributeValueFuser<List<String>, Song, Attribute> {

    public Genres_Fuser() {super(new Union<String, Song, Attribute>());}

    @Override
    public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Song.Album_Genres);
    }

    @Override
    public List<String> getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return Arrays.asList(record.getAlbum_genres());
    }

    @Override
    public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<List<String>, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setAlbum_genres(fused.getValue().toArray(new String[0]));
        fusedRecord.setAttributeProvenance(Song.Album_Genres,
                fused.getOriginalIds());
    }
}
