package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.numeric;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;
import java.util.Iterator;

public class Max<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Double, RecordType, SchemaElementType> {

    //Max conflict resoluter created for attributes: "Tempo" and "Duration"
    public Max() {
    }

    public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {
        if (values.size() == 0) {
            return new FusedValue((Double)null);
        } else {
            double count = 0.0;
            double max_value = 0;
            for(Iterator i = values.iterator(); i.hasNext(); ++count) {
                FusibleValue<Double, RecordType, SchemaElementType> value = (FusibleValue)i.next();
                if((Double)value.getValue() > max_value){
                    max_value = (Double)value.getValue();
                }
            }

            return new FusedValue(max_value);
        }
    }
}