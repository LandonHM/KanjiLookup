package org.landon.kanji.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document("meaning")
public class KanjiMeaning {

    @Id
    private String id;

    private String literal;
    private Map<String, String> codepoint;
    private Map<String, String> radical;
    private Map<String, String> misc;
    private Map<String, String> dic_number;
    private Map<String, String> query_code;
    private Map<String, String> reading;
    private Map<String, String> meaning;
    private String nanori;


    public KanjiMeaning(String id, String literal, Map<String, String> codepoint, Map<String, String> radical, Map<String, String> misc, Map<String, String> dic_number, Map<String, String> query_code, Map<String, String> reading, Map<String, String> meaning, String nanori) {
        this.id = id;
        this.literal = literal;
        this.codepoint = codepoint;
        this.radical = radical;
        this.misc = misc;
        this.dic_number = dic_number;
        this.query_code = query_code;
        this.reading = reading;
        this.meaning = meaning;
        this.nanori = nanori;
    }

    public String getId() {
        return id;
    }

    public String getLiteral() {
        return literal;
    }

    public Map<String, String> getCodepoint() {
        return codepoint;
    }

    public Map<String, String> getRadical() {
        return radical;
    }

    public Map<String, String> getMisc() {
        return misc;
    }

    public Map<String, String> getDic_number() {
        return dic_number;
    }

    public Map<String, String> getQuery_code() {
        return query_code;
    }

    public Map<String, String> getReading() {
        return reading;
    }

    public Map<String, String> getMeaning() {
        return meaning;
    }

    public String getNanori() {
        return nanori;
    }
}
