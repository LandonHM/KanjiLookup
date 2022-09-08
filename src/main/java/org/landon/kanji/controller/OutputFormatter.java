package org.landon.kanji.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class which outputs data into a table for easier reading in terminal
 */
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

    public void addSection(String title, Map<String, String[]> data, Lang lang){
        Section temp = new Section(title, data, lang);
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
             ┌────┐
             │ 早 │
             └────┘
┌──────────────────┬──────────────┐
│ Section[0] title │              │
│                  │              │
│ DataKey[0]       │ DataEntry[0] │
│                  │              │
│ DataKey[1]       │ DataEntry[0] │
│                  │ DataEntry[n] │
├──────────────────┼──────────────┤
│ Section[1] title │              │
│                  │              │
│ DataKey[0]       │ DataEntry[0] │
│                  │              │
│ DataKey[1]       │ DataEntry[0] │
│                  │ DataEntry[n] │
└──────────────────┴──────────────┘
     */

    public String toString(){
        // Must have at least one section
        //if(sections.size() == 0)
        //return null;


        // Used to build the output
        StringBuilder out = new StringBuilder("\n");

        // Title box at top, should look like below (centered at the top)
        //┌────┐
        //│ 早 │
        //└────┘
        // Kanji are two wide i guess
        int padding = ((maxHeadLen + maxDataLen) / 2)+1;
        out.append(String.format("%" + padding + "s┌────┐%n", ""));
        out.append(String.format("%" + padding + "s│ %s │%n", "", title));
        out.append(String.format("%" + padding + "s└────┘%n", ""));

        //Empty line will be used quite often so just save it.
        String emptyLine = String.format(" │%1$" + (2+maxHeadLen) + "s│%1$" + (maxDataLen+2) + "s│%n", "");

        //Top line
        out.append(" ");
        out.append(String.format("┌%1$"+ (2+maxHeadLen) +"s┬%1$" + (2+maxDataLen) + "s┐%n", "").replace(" ", "─"));
        int langPad = 0;
        String data;
        for(Section s : sections){
            out.append(String.format(" │ %-" + maxHeadLen + "s │ %"+ maxDataLen + "s │%n", s.getTitle(), ""));
            // Assumes string list has at least one element
            for(Map.Entry<String, String[]> e : s.getData().entrySet()){
                out.append(emptyLine);
                if(s.getLang() == Lang.EN) {
                    out.append(String.format(" │ %-" + maxHeadLen + "s │ %-" + maxDataLen + "s │%n", e.getKey(), e.getValue()[0]));
                    for (int i = 1; i < e.getValue().length; i++) {
                        out.append(String.format(" │ %" + maxHeadLen + "s │ %-" + maxDataLen + "s │%n", "", e.getValue()[i]));
                    }
                }else{
                    data = e.getValue()[0];
                    langPad = (data.indexOf('.') >= 0 ? 1 : 0) + (data.indexOf('-') >= 0 ? 1 : 0);
                    out.append(String.format(" │ %-" + maxHeadLen + "s │ %-" + (maxDataLen - data.length() + langPad) + "s │%n", e.getKey(), e.getValue()[0]));
                    for (int i = 1; i < e.getValue().length; i++) {
                        data = e.getValue()[i];
                        langPad = (data.indexOf('.') >= 0 ? 1 : 0) + (data.indexOf('-') >= 0 ? 1 : 0);
                        out.append(String.format(" │ %" + maxHeadLen + "s │ %-" + (maxDataLen - data.length() + langPad) + "s │%n", "", e.getValue()[i]));
                    }
                }
            }
            out.append(" ");
            out.append(String.format("├%1$" + (2+maxHeadLen) + "s┼%1$" + (2+maxDataLen) + "s┤%n", "").replace(" ", "─"));
        }

        // Replace the last "├ ┼ ┤" with "└ ┴ ┘" to finish the box
        int length = out.length();
        out.setCharAt(length-2, '┘');
        out.setCharAt(length-maxDataLen-5, '┴');
        out.setCharAt(length-maxDataLen-maxHeadLen-8, '└');

        return out.toString();
    }
}

class Section {

    private int maxHeadLen;
    private int maxDataLen;
    private String title;
    private Map<String, String[]> data;

    private Lang lang;

    // Handle lang differently, jp characters are two wide.
    public Section(String title, Map<String, String[]> data, Lang lang){
        this.title = title;
        this.data = data;
        this.lang = lang;
        maxHeadLen = title.length();
        maxDataLen = title.length();
        int len = 0;
        for(Map.Entry<String,String[]> e : data.entrySet()) {

            if(e.getKey().length() > maxHeadLen)
                maxHeadLen = e.getKey().length();

            for(String s : e.getValue()){
                if(lang == Lang.JP){
                    // If Japanese text, then the print size will be double the length (minus any non double wide characters there may be)
                    len = 2*s.length();
                    if(s.indexOf('.') >= 0)
                        len--;
                    if(s.indexOf('-') >= 0)
                        len--;
                }else{
                    // If english text, then the print size and length are equal
                    len = s.length();
                }
                if(len > maxDataLen)
                    maxDataLen = len;
            }

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

    public Lang getLang() {
        return lang;
    }
}
