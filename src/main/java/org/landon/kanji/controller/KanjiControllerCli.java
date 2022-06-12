package org.landon.kanji.controller;

import org.landon.kanji.model.Input;
import org.landon.kanji.model.KanjiMeaning;
import org.landon.kanji.model.KanjiRadicals;
import org.landon.kanji.repository.MeaningRepository;
import org.landon.kanji.repository.RadicalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(value="/")
public class KanjiControllerCli {

    @Autowired
    private MeaningRepository meaningRepo;

    @Autowired
    private RadicalsRepository radicalRepo;

    public KanjiControllerCli(){

    }

    @GetMapping(value = "*")
    public @ResponseBody String getKanjiFallback(){
        return "\u001b[31mError:\u001b[0m Only /{char here} is a valid request.\n";
    }

    @GetMapping(value = "/{kanji}")
    public @ResponseBody String getKanji(@PathVariable(value = "kanji", required = true) String kanji){
        KanjiMeaning k = meaningRepo.findMeaningByLiteral(kanji);
        KanjiRadicals r = radicalRepo.findRadicalsByKanji(kanji);
        // Cannot find, return error.
        if(k == null || r == null)
            return "\u001b[31mError:\u001b[0m Character \"" + kanji + "\" not found.\n";

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

    //@CrossOrigin()
    @GetMapping(value = "/{kanji}", produces = {"application/json", "application/xml"})
    public @ResponseBody
    KanjiMeaning getKanjiApplication(@PathVariable(value = "kanji", required = true) String kanji){
        return meaningRepo.findMeaningByLiteral(kanji);
    }
}
