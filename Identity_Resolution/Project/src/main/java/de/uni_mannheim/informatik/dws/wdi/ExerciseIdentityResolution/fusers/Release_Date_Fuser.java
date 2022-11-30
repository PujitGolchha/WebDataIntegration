package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta.MostRecent;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.time.LocalDateTime;

public class Release_Date_Fuser extends AttributeValueFuser<LocalDateTime, Song, Attribute> {

    public Release_Date_Fuser() {
        super(new MostRecent<LocalDateTime, Song, Attribute>());
    }
    @Override
    public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Song.Release_Date);
    }
    @Override
    public LocalDateTime getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return  record.getRelease_date();
    }
    @Override
    public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<LocalDateTime, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setRelease_date(fused.getValue());
        fusedRecord.setAttributeProvenance(Song.Release_Date,
                fused.getOriginalIds());
    }
}
