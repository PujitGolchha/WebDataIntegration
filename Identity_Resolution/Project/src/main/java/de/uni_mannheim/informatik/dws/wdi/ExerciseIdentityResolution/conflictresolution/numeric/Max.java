//package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.numeric;
//
//import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
//import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
//import de.uni_mannheim.informatik.dws.winter.model.Fusible;
//import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
//import de.uni_mannheim.informatik.dws.winter.model.Matchable;
//
//import java.util.Collection;
//import java.util.Iterator;
//
//public class Max<Double, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<Double, RecordType, SchemaElementType> {
//    public Max(){}
//    public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {
//
//        if (values.size() == 0) {
//            return new FusedValue((Double)null);
//        } else {
//            int count = 0;
//            FusibleValue<Double, RecordType, SchemaElementType> max_value = null;
//            for(Iterator i = values.iterator(); i.hasNext(); ++count) {
//                System.out.println("Values");
//                FusibleValue<Float, RecordType, SchemaElementType> value = (FusibleValue)i.next();
//                System.out.println((Float)value.getValue());
//                if((Double)value.getValue() > (Double)max_value.getValue()){
//                    max_value = value;
//                }
//            }
//
//            return new FusedValue(max_value);
//
//        }
//    }
//}