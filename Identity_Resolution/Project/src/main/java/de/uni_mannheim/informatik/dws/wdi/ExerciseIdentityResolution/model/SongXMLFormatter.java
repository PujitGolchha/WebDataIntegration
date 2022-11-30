package de.uni_mannheim.informatik.dws.wdi.ExerciseIdentityResolution.model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class SongXMLFormatter extends XMLFormatter<Song> {
    public Element createRootElement(Document doc) {
        return doc.createElement("Songs");
    }


    @Override
    public Element createElementFromRecord(Song record, Document doc) {
        Element song = doc.createElement("song");

        song.appendChild(createTextElement("id", record.getIdentifier(), doc));

        song.appendChild(createTextElementWithProvenance("track_name",
                record.getTrack_name(),
                record.getMergedAttributeProvenance(Song.Track_Name), doc));
        song.appendChild(createTextElementWithProvenance("album_name",
                record.getAlbum_name(),
                record.getMergedAttributeProvenance(Song.Album_Name), doc));
        song.appendChild(createTextElementWithProvenance("release_date",
                record.getRelease_date().toString(),
                record.getMergedAttributeProvenance(Song.Release_Date), doc));

        song.appendChild(createTextElementWithProvenance("artists", record
                .getArtists().toString(), record
                .getMergedAttributeProvenance(Song.Artists), doc));

        song.appendChild(createTextElementWithProvenance("album_genres", record
                .getAlbum_genres().toString(), record
                .getMergedAttributeProvenance(Song.Album_Genres), doc));

        song.appendChild(createTextElementWithProvenance("tempo", Float.toString(record.getTempo())
                ,record
                .getMergedAttributeProvenance(Song.Tempo), doc));

        song.appendChild(createTextElementWithProvenance("duration", Float.toString(record.getDuration())
                ,record
                        .getMergedAttributeProvenance(Song.Duration), doc));

        return song;
    }

    protected Element createTextElementWithProvenance(String name,
                                                      String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

}
