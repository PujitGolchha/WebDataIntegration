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

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

/**
 * A {@link AbstractRecord} representing a movie.
 *
 * @author Oliver Lehmberg (oli@dwslab.de)
 *
 */
public class Song implements Matchable {

    /*
     * example entry <movie> <id>academy_awards_2</id> <title>True Grit</title>
     * <director> <name>Joel Coen and Ethan Coen</name> </director> <actors>
     * <actor> <name>Jeff Bridges</name> </actor> <actor> <name>Hailee
     * Steinfeld</name> </actor> </actors> <date>2010-01-01</date> </movie>
     */

    protected String id;
    protected String provenance;
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

    public Song(String identifier, String provenance) {
        id = identifier;
        this.provenance = provenance;
    }

    @Override
    public String getIdentifier() {
        return id;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

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
        this.artists = artists;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isExplicit() {
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
        this.duration = duration;
    }

    ///

    @Override
    public String toString() {
        return String.format("[Movie %s: %s / %s / %s / %s]", getIdentifier(), getTrack_name(),
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
