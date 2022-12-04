package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.fusers;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.numeric.Max;
import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class Tempo_Fuser extends AttributeValueFuser<Double, Song, Attribute>  {

    public Tempo_Fuser () {
        super(new Max<Song, Attribute>());
    }
    @Override
    public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return record.hasValue(Song.Tempo);
    }
    @Override
    public Double getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
        return  (double)record.getTempo();
    }
    @Override
    public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
        FusedValue<Double, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
        fusedRecord.setTempo(fused.getValue().floatValue());
        fusedRecord.setAttributeProvenance(Song.Tempo,
                fused.getOriginalIds());
    }
}
