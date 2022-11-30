/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.Comparators;

import de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model.Song;
import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.comparators.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Comparator} for {@link Song}s based on the {@link Song#getTrack_name()} ()}
 * value and their {@link} value.
 * 
 * @author Sharan, Aakriti
 * 
 */
public class SongTrackAnomalityComparatorLevenshteinSimilarity implements Comparator<Song, Attribute> {

	private static final long serialVersionUID = 1L;
	private LevenshteinSimilarity sim = new LevenshteinSimilarity();
	private ComparatorLogger comparisonLog;

	@Override
	public double compare(
			Song record1,
			Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondences) {
		
		String s1 = record1.getTrack_name().toLowerCase();
		String s2 = record2.getTrack_name().toLowerCase();
		String s3 = record1.getTrack_name().toLowerCase().replaceAll("[(\\[{].*[)\\]}]","");
		s3 = s3.replaceAll("\\p{Punct}","");
		String s4 = record2.getTrack_name().toLowerCase().replaceAll("[(\\[{].*[)\\]}]","");
		s4 = s4.replaceAll("\\p{Punct}","");
		double similarity1 = 0.0;
		if(s3.equals(s4)){
			Pattern p = Pattern.compile("[(\\[{].*[)\\]}]");
			Matcher m1 = p.matcher(s1);
			Matcher m2 = p.matcher(s2);
			if (m1.find() && !m2.find())
			{
				String edition1 = m1.group(1);
				if(edition1.contains("remaster")){similarity1= sim.calculate(s1, s2);}
				else similarity1 = 1.0;
			}
			else if (m2.find() && !m1.find()) {
				String edition2 = m2.group(1);
				if(edition2.contains("remaster")){similarity1= sim.calculate(s1, s2);}
				else similarity1 = 1.0;
			}
			else{
				String edition1 = m1.group(1);
				String edition2 = m2.group(1);
				if(edition1.contains("remaster") && edition2.contains("remaster")){similarity1= 1.0;}
				else {similarity1 = sim.calculate(s1, s2);}
			}
		}
		else{
			 similarity1 = sim.calculate(s1, s2);
		}
		
		if(this.comparisonLog != null){
			this.comparisonLog.setComparatorName(getClass().getName());
			this.comparisonLog.setRecord1Value(s1);
			this.comparisonLog.setRecord1PreprocessedValue(s3);
			this.comparisonLog.setRecord2Value(s2);
			this.comparisonLog.setRecord2PreprocessedValue(s4);
			this.comparisonLog.setSimilarity(Double.toString(similarity1));
			this.comparisonLog.setPostprocessedSimilarity(Double.toString(similarity1));
		}
		
		return similarity1;
		
	}

	@Override
	public ComparatorLogger getComparisonLog() {
		return this.comparisonLog;
	}

	@Override
	public void setComparisonLog(ComparatorLogger comparatorLog) {
		this.comparisonLog = comparatorLog;
	}

}
