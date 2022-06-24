package org.landon.kanji.controller;

import org.landon.kanji.model.KanjiMeaning;
import org.landon.kanji.model.KanjiRadicals;
import org.landon.kanji.repository.MeaningRepository;
import org.landon.kanji.repository.RadicalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Class which handles requests with no headers (default option for curl)
 * Returns output as raw strings which are to be read in command line
 * Also can return xml or json if request with application/{xml/json} if needed
 */
@Controller
@RequestMapping(value="/")
public class KanjiControllerCli {

    /**
     * Repository which holds data relating to the meaning and readings of kanji
     */
    @Autowired
    private MeaningRepository meaningRepo;

    /**
     * Repository which holds the radicals which make up a kanji character
     */
    @Autowired
    private RadicalsRepository radicalRepo;

    public KanjiControllerCli(){

    }

    /**
     * Default handler, returns help message
     * @return String of data showing how the user search the data they need
     */
    @GetMapping(value = "*")
    public @ResponseBody String getKanjiFallback(){
        return "\nUsage (replace kanji with either a kanji character or a url encoded kanji character) :" +
                "\n\t$ curl {url}/{kanji}" +
                "\n\n\t$ curl {url}/ref/{kanji}" +
                "\n\n";
    }

    /**
     * Returns help
     * @return String which defines what is returned from a search
     */
    @GetMapping(value = "/help")
    public @ResponseBody String getKanjiHelp(){
        return "";
    }

    /**
     * Gives a string of data relating to a kanji character
     * @param kanji Kanji character which user is looking up
     * @return String of data relating to the kanji character
     */
    @GetMapping(value = "/{kanji}")
    public @ResponseBody String getKanji(@PathVariable(value = "kanji", required = true) String kanji){
        KanjiMeaning k = meaningRepo.findMeaningByLiteral(kanji);
        KanjiRadicals r = radicalRepo.findRadicalsByKanji(kanji);
        // Cannot find, return error.
        if(k == null || r == null)
            return "\n\u001b[31mError:\u001b[0m Character \"" + kanji + "\" not found.\n\n";

        //Data from meaning
        String literal = k.getLiteral();
        Map<String, String> codepoint = k.getCodepoint();
        Map<String, String> misc = k.getMisc();
        Map<String, String> reading = k.getReading();
        Map<String, String> meaning = k.getMeaning();
        String nanori = k.getNanori();
        String temp;

        //Data from radicals
        String radicals = r.getRadicals();

        //Return message to console.
        String out = "\t\t" + k.getLiteral() + "   (" + misc.get("stroke_count") + " strokes)\n";
        out += "Radicals:\n\t" + radicals + "\n";

        out += "Readings: \n";
        temp = reading.get("ja_on");
        out += temp != null ? "\tOn: " + temp + "\n": "";
        temp = reading.get("ja_kun");
        out += temp != null ? "\tKun: " + temp + "\n": "";

        out += "Meaning: \n";
        temp = meaning.get("en");
        out += temp != null ? "\t" + temp + "\n" : "";

        out += nanori != null ? "Name Readings: " + nanori + "\n" : "";

        out += "Misc: \n";
        temp = misc.get("grade");
        out += temp != null ? "\tGrade: " + temp + "\n": "";
        temp = misc.get("freq");
        out += temp != null ? "\tFrequency: " + temp + "/2500 in newspapers\n": "";
        temp = misc.get("jlpt");
        out += temp != null ? "\tJLPT Level: " + temp + "\n": "";

        return out;
    }

    /**
     * Gives xml/json data relating to the meaning of a kanji character
     * @param kanji Kanji character which user is looking up
     * @return returns object of kanjimeaning which will auto be formatted to xml/json depending on the request
     */
    //@CrossOrigin()
    @GetMapping(value = "/{kanji}", produces = {"application/json", "application/xml"})
    public @ResponseBody
    KanjiMeaning getKanjiApplication(@PathVariable(value = "kanji", required = true) String kanji){
        return meaningRepo.findMeaningByLiteral(kanji);
    }

    /**
     * Returns svg file of a kanji indicating proper stroke order
     * @param kanji Kanji character which user is looking up
     * @return A resource which is an svg file of the specified kanji
     */
    @GetMapping(value = "/svg/{kanji}")
    public ResponseEntity<Resource> getKanjiSvg(@PathVariable(value = "kanji", required = true) String kanji) {
        String fileName = String.format("%1$05x.svg", (int) kanji.charAt(0));

        String baseDir = "/home/landon/IdeaProjects/kanji/src/main/resources/static/images/";
        File file = new File(baseDir+fileName);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
        try{
            resource = new ByteArrayResource(Files.readAllBytes(path));
        }catch (Exception e){
            System.out.println("\n\nError\n\n");
        }

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .body(resource);

    }

    /**
     * Gives a string of data relating to a kanji character's dictionary references
     * @param kanji Kanji character which user is looking up
     * @return Formatted string of related references
     */
    @GetMapping(value = {"/ref/{kanji}","/dic/{kanji}"})
    public @ResponseBody String getKanjiRef(@PathVariable(value = "kanji", required = true) String kanji){
        KanjiMeaning k = meaningRepo.findMeaningByLiteral(kanji);

        // Cannot find, return error.
        if(k == null)
            return "\n\u001b[31mError:\u001b[0m Character \"" + kanji + "\" not found.\n\n";

        // Data for dictionary references
        Map<String, String> ref = k.getDic_number();

        //Return message to console.
        String out = "\n";
        out += String.format("%1$31s+----+%1$31s%n", "");
        out += String.format("%1$31s| %2$s | %1$31s%n", "", kanji.charAt(0));
        out += String.format("%1$31s+----+%1$31s%n", "");
        String line = String.format("+%65s+%n", "").replace(" ", "-");
        out += line;
        for(Map.Entry<String,String> e : ref.entrySet()){
            out += String.format("| %1$-54s : %2$-6s |%n",Dictionary.map.get(e.getKey()),e.getValue());
        }
        out += line + "\n";
        return out;
    }

    /**
     * Gives xml/json data relating to the references of a kanji character
     * @param kanji Kanji character which user is looking up
     * @return returns map which will auto be formatted to xml/json depending on the request
     */
    @GetMapping(value = "/ref/{kanji}", produces = {"application/json", "application/xml"})
    public @ResponseBody Map<String,String> getKanjiApplicationRef(@PathVariable(value = "kanji", required = true) String kanji){
        return meaningRepo.findMeaningByLiteral(kanji).getDic_number();
    }
}
