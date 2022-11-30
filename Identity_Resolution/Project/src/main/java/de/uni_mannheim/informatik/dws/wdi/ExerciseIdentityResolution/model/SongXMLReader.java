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
package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * A {@link XMLMatchableReader} for {@link Song}s.
 * 
 * @author Oliver Lehmberg (oli@dwslab.de)
 * 
 */
public class SongXMLReader extends XMLMatchableReader<Song, Attribute> implements FusibleFactory<Song, Attribute> {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Song, Attribute> dataset) {
		super.initialiseDataset(dataset);

		// the schema is defined in the Song class and not interpreted from the file, so we have to set the attributes manually
		dataset.addAttribute(Song.Track_Name);
		dataset.addAttribute(Song.Album_Name);
		dataset.addAttribute(Song.Album_Genres);
		dataset.addAttribute(Song.Artists);
		dataset.addAttribute(Song.Duration);
		dataset.addAttribute(Song.Release_Date);
		dataset.addAttribute(Song.Tempo);
//		dataset.addAttribute(Song.Popularity);
//		dataset.addAttribute(Song.Album_Type);
//		dataset.addAttribute(Song.Explicit);


	}

//	private static String getValue(String tag, Element element) {
//		NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
//		Node node = (Node) nodes.item(0);
//		return node.getNodeValue();
//	}

	protected void getChildElements(Node node, String childName, Song song) {
		NodeList children = node.getChildNodes();

		for(int j = 0; j < children.getLength(); ++j) {
			Node child = children.item(j);
			if (child.getNodeType() == 1 && child.getNodeName().equals(childName)) {
				song.setAlbum_name(getValueFromChildElement(child,"name"));
				song.setAlbum_type(getValueFromChildElement(child,"type"));
				if(getValueFromChildElement(child, "genres") != null){
					song.setAlbum_genres(getValueFromChildElement(child, "genres").replaceAll("\'", "").replaceAll("\\[", "").replaceAll("\\]","").split(", "));
				}
				else{
					song.setAlbum_genres(new String[]{"-"});
				}
			}
		}
	}

	protected void modifyArtists(Node node, String childname, Song song){
		String artists = getValueFromChildElement(node, childname);
		String[] art_array = artists.replaceAll("\'", "").replaceAll("\\[", "").replaceAll("\\]","").split(", ");
		song.setArtists(art_array);
	}

	@Override
	public Song createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Song song = new Song(id, provenanceInfo);

		// fill the attributes
		song.setTrack_name(getValueFromChildElement(node, "track_name"));

		song.setAlbum_name(getValueFromChildElement(node, "name"));

		getChildElements(node, "album",song);
		modifyArtists(node, "artists",song);

		if (getValueFromChildElement(node, "duration") == null)
		{
			song.setDuration(Integer.parseInt("-1"));
		}
		else {
			song.setDuration(Integer.parseInt(getValueFromChildElement(node, "duration")));
		}

		if (getValueFromChildElement(node, "popularity") == null)
		{
			song.setPopularity(Integer.parseInt("-1"));
		}
		else {
			song.setPopularity(Integer.parseInt(getValueFromChildElement(node, "popularity")));
		}

		if (getValueFromChildElement(node, "Tempo") == null)
		{
			song.setTempo(Float.parseFloat("-1"));
		}
		else {
			song.setTempo(Float.parseFloat(getValueFromChildElement(node, "Tempo")));
		}

		if (getValueFromChildElement(node, "explicit") == null)
		{
			song.setExplicit(Boolean.parseBoolean("false"));
		}
		else {
			song.setExplicit(Boolean.parseBoolean(getValueFromChildElement(node, "explicit")));
		}


		// convert the date string into a DateTime object
		try {
			String release_date = getValueFromChildElement(node, "release_date");
			if (release_date != null && !release_date.isEmpty()) {
				DateTimeFormatter formatter = new DateTimeFormatterBuilder()
				        .appendPattern("yyyy-MM-dd")
				        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
				        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				        .toFormatter(Locale.ENGLISH);
				LocalDateTime dt = LocalDateTime.parse(release_date, formatter);
				song.setRelease_date(dt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return song;
	}
	public Song createInstanceForFusion(RecordGroup<Song, Attribute> cluster) {

		List<String> ids = new LinkedList<>();

		for (Song m : cluster.getRecords()) {
			ids.add(m.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Song(mergedId, "fused");
	}

}
