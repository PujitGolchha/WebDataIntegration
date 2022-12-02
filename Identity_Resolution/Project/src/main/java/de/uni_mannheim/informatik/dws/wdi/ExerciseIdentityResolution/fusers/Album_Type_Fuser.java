package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class Album_Type_Fuser extends AttributeValueFuser<String, Song, Attribute> {

    public Album_Type_Fuser() {
        super(new FavourSources<>());
    }

    @Override
    public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Song.Album_Type);
    }

    @Override
    public String getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.getAlbum_type();
    }

    @Override
    public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<String, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setAlbum_name(fused.getValue());
        fusedRecord.setAttributeProvenance(Song.Album_Type,
                fused.getOriginalIds());
    }
}
