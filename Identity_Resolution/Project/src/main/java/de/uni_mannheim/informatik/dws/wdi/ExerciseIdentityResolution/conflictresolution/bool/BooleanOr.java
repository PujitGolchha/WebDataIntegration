package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.bool;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;
import java.util.Iterator;

public class BooleanOr<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Boolean, RecordType, SchemaElementType> {
    public BooleanOr() {
    }

    public FusedValue<Boolean, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Boolean, RecordType, SchemaElementType>> values) {
        if (values.size() == 0) {
            return new FusedValue(false);
        } else {
            boolean result = false;
            Iterator i = values.iterator();
            do{
                FusibleValue value;
                value = (FusibleValue) i.next();
                boolean currResult = (boolean)value.getValue();

                if (currResult == true){
                    result = true;
                }
            }while(i.hasNext() && result != true);
            return new FusedValue(result);
        }
    }
}