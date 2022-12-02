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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import weka.core.pmml.jaxbbindings.False;

/**
 * A {@link AbstractRecord} representing a movie.
 *
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class Song extends AbstractRecord<Attribute> implements Serializable {

    /*
     * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
     * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
     * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
     * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
     */
    private static final long serialVersionUID = 1L;
    public Song(String identifier, String provenance) {
        super(identifier, provenance);
    }
    private String track_name;
    private LocalDateTime release_date;
    private String album_name;
    private String album_type;
    private String[] album_genres;
    private String[] artists;
    private int popularity;
    private boolean explicit;
    private float tempo;
    private int duration;

    private Map<Attribute, Collection<String>> provenance = new HashMap<>();
    private Collection<String> recordProvenance;

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public LocalDateTime getRelease_date() {
        return release_date;
    }

    public void setRelease_date(LocalDateTime release_date) {
        this.release_date = release_date;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }

    public String[] getAlbum_genres() {
        return album_genres;
    }

    public void setAlbum_genres(String[] album_genres) {
            this.album_genres = album_genres;
    }

    public String[] getArtists() {
        return artists;
    }

    public void setArtists(String[] artists) {
        for(int i = 0; i< artists.length; i++){
           artists[i] = artists[i].toLowerCase();
        }
        this.artists = artists;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(boolean explicit) {
        this.explicit = explicit;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(float tempo) {
        this.tempo = tempo;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if (Integer.toString(duration).length()>=6)
        {
            this.duration = duration / 1000;
        }
        else
        {
            this.duration = duration;
        }
    }

    public Collection<String> getRecordProvenance() {
        return recordProvenance;
    }

    public void setRecordProvenance(Collection<String> provenance) {
        recordProvenance = provenance;
    }

    public void setAttributeProvenance(Attribute attribute,
                                       Collection<String> provenance) {
        this.provenance.put(attribute, provenance);
    }


    public Collection<String> getAttributeProvenance(String attribute) {
        return provenance.get(attribute);}

    public String getMergedAttributeProvenance(Attribute attribute) {
        Collection<String> prov = provenance.get(attribute);

        if (prov != null) {
            return StringUtils.join(prov, "+");
        } else {
            return "";
        }
    }
    ///
    public static final Attribute Track_Name = new Attribute("Track_Name");
    public static final Attribute Album_Name = new Attribute("Album_Name");
    public static final Attribute Release_Date = new Attribute("Release_Date ");
    public static final Attribute Duration= new Attribute("Duration");

    public static final Attribute Artists= new Attribute("Artists");

    public static final Attribute Tempo= new Attribute("Tempo");

    public static final Attribute Album_Genres= new Attribute("Album_Genres");

    public static final Attribute Album_Type= new Attribute("Album_Type");

    public static final Attribute Explicit= new Attribute("Explicit");

    @Override
    public boolean hasValue(Attribute attribute) {
        if(attribute == Track_Name)
            return getTrack_name() != null && !getTrack_name().isEmpty();
        else if(attribute==Album_Name)
            return getAlbum_name() != null && !getAlbum_name().isEmpty();
        else if(attribute==Release_Date)
            return !(getRelease_date().getYear()==1900);
        else if(attribute==Duration)
            return getDuration() != -1;
        else if(attribute==Artists)
            return ArrayUtils.isNotEmpty(getArtists());
        else if(attribute==Tempo)
            return getTempo() != -1.0;
        else if(attribute==Album_Genres)
            return ArrayUtils.isNotEmpty(getAlbum_genres());
        else if(attribute==Album_Type)
            return !getAlbum_type().equals("");

        // Boolean explicit will always have a value as set in SongXMLReader (default false)
        else if(attribute==Explicit)
            return getExplicit() || !getExplicit();

        else
            return false;
    }

    @Override
    public String toString() {
        return String.format("[Song %s: %s / %s / %s / %s]", getIdentifier(), getTrack_name(),
                getAlbum_name(), Arrays.toString(getArtists()), getRelease_date().toString());
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Song){
            return this.getIdentifier().equals(((Song) obj).getIdentifier());
        }else
            return false;
    }

}
