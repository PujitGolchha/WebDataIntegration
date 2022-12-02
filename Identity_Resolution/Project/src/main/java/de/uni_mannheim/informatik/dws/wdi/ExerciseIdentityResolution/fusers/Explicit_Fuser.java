package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.bool.BooleanOr;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.FavourSources;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class Explicit_Fuser extends AttributeValueFuser<Boolean, Song, Attribute>  {

    public Explicit_Fuser() {
        super(new BooleanOr<>());
    }
    @Override
    public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Song.Explicit);
    }
    @Override
    public Boolean getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return  record.getExplicit();
    }
    @Override
    public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Boolean, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setExplicit(fused.getValue());
        fusedRecord.setAttributeProvenance(Song.Explicit,
                fused.getOriginalIds());
    }
}
