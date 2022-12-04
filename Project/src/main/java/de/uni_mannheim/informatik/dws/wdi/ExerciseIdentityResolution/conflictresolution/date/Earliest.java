package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.conflictresolution.date;

//package de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.meta;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import org.apache.jena.base.Sys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;

public class Earliest<ValueType, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<ValueType, RecordType, SchemaElementType> {

    //Date Conflict resoluter created for attribute "release date" - selects earliest date after ignoring
    //dates with year "1900"
    public Earliest() {
    }

    public FusedValue<ValueType, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<ValueType, RecordType, SchemaElementType>> values) {
        FusibleValue<ValueType, RecordType, SchemaElementType> mostRecent = null;
        Iterator var3 = values.iterator();
        String dateString = "1900-01-01 00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime defaultDate = LocalDateTime.parse(dateString, formatter);

        mostRecent = (FusibleValue)var3.next();
          do{
              FusibleValue value;
              value = (FusibleValue)var3.next();
              LocalDateTime mostRecentTime =  (LocalDateTime) mostRecent.getValue();
              LocalDateTime valueTime =  (LocalDateTime) value.getValue();
              if(mostRecentTime.getYear() == 1900){
                  mostRecentTime = valueTime;
                  int a =1;
              }
              if((mostRecentTime.isAfter(valueTime) || mostRecentTime.isEqual(valueTime)) && !(valueTime.getYear()==1900)){
                  mostRecent = value;
                  int a = 1;
              }
         }while(var3.hasNext());

          return new FusedValue(mostRecent);
    }
}
