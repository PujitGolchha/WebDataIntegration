package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.numeric;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;
import java.util.Iterator;

public class Min<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Double, RecordType, SchemaElementType> {
    public Min(){}
    public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {
        if (values.size() == 0) {
            return new FusedValue((Double)null);
        } else {
            double count = 0.0;
            double min_value = Integer.MAX_VALUE;
            for(Iterator i = values.iterator(); i.hasNext(); ++count) {
                FusibleValue<Double, RecordType, SchemaElementType> value = (FusibleValue)i.next();
                if((Double)value.getValue() < min_value){
                    min_value = (Double)value.getValue();
                }
            }

            return new FusedValue(min_value);

        }
    }
}