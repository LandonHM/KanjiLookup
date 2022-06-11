package org.landon.kanji.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("radicals")
public class KanjiRadicals {

    @Id
    private String id;

    private String kanji;
    private String radicals;


    public KanjiRadicals(String id, String kanji, String radicals) {
        this.id = id;
        this.kanji = kanji;
        this.radicals = radicals;
    }

    public String getId() {
        return id;
    }

    public String getKanji() {
        return kanji;
    }

    public String getRadicals() {
        return radicals;
    }
}
