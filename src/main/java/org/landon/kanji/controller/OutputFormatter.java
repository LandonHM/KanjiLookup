package org.landon.kanji.controller;

import org.eclipse.jetty.util.ArrayTernaryTrie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OutputFormatter {
    private String title;
    private List<Section> sections;

    private int maxHeadLen;
    private int maxDataLen;

    //Assumes title is shorter than the combination of the header and data length
    public OutputFormatter(String title){
        this.title = title;
        sections = new ArrayList<Section>();
    }

    public void addSection(String title, Map<String, String[]> data){
        Section temp = new Section(title, data);
        sections.add(temp);
        updateMaxLen(temp);
    }

    public void addSection(Section s){
        sections.add(s);
        updateMaxLen(s);
    }

    private void updateMaxLen(Section s){
        if(s.getMaxDataLen() > maxDataLen)
            maxDataLen = s.getMaxDataLen();
        if(s.getMaxHeadLen() > maxHeadLen)
            maxHeadLen = s.getMaxHeadLen();
    }

    /* Example out
                        ┌─────────────┐
┌───────────────────────┤    Title    ├───────────────────┐
│ Section[0] title      └──────┬──────┘                   │
│                              │                          │
│ DataKey[0]                   │ DataEntry[0]             │
│                              │                          │
│ DataKey[1]                   │ DataEntry[0]             │
│                              │ DataEntry[n]             │
├──────────────────────────────┼──────────────────────────┤
│ Section[1] title             │                          │
│                              │                          │
│ DataKey[0]                   │ DataEntry[0]             │
│                              │                          │
│ DataKey[1]                   │ DataEntry[0]             │
│                              │ DataEntry[n]             │
└──────────────────────────────┴──────────────────────────┘


     */

    public String toString(){
        // Must have at least one section
        if(sections.size() == 0)
            return null;
        //Empty line will be used quite often so just save it.
        String emptyLine = String.format("│%1$" + (2+maxHeadLen) + "s│%1$" + (maxDataLen+2) + "s|%n", "");

        //Format title.

        return "";
    }
}

class Section {

    private int maxHeadLen;
    private int maxDataLen;
    private String title;
    private Map<String, String[]> data;

    public Section(String title, Map<String, String[]> data){
        this.title = title;
        this.data = data;
        maxHeadLen = -1;
        maxDataLen = -1;
        for(Map.Entry<String,String[]> e : data.entrySet()) {
            if(e.getKey().length() > maxHeadLen)
                maxHeadLen = e.getKey().length();
            for(String s : e.getValue())
                if(s.length() > maxDataLen)
                    maxDataLen = s.length();
        }
    }

    public int getMaxDataLen() {
        return maxDataLen;
    }

    public int getMaxHeadLen() {
        return maxHeadLen;
    }

    public Map<String, String[]> getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }
}
