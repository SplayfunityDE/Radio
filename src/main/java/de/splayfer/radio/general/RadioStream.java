package de.splayfer.radio.general;

import de.splayfer.radio.MongoDBDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class RadioStream {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public RadioStream(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static RadioStream getRadioStream(String name) {
        return getFromDocument(mongoDB.find("radio", "name", name).first());
    }

    public static RadioStream getFromDocument(Document document) {
        return new RadioStream(document.getString("name"), document.getString("url"));
    }

    public static List<RadioStream> getAllRadioStreams() {
        List<RadioStream> radioStreams = new ArrayList<>();
        mongoDB.findAll("radio").forEach(radioStream -> radioStreams.add(getFromDocument(radioStream)));
        return radioStreams;
    }

    public void insertToMongoDB() {
        mongoDB.insert("radio", new Document("name", name).append("url", url));
    }
}
