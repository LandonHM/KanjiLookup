package org.landon.kanji.model;

/**
 * Used to store the user input in the 'search.html' page.
 */
public class Input {
    public String kanji;

    public Input(){

    }

    public Input(String s){
        kanji = s;
    }

    public String getKanji(){
        return kanji;
    }

    public void setKanji(String kanji){
        this.kanji = kanji;
    }
}
