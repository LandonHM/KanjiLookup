package org.landon.kanji.controller;

import org.landon.kanji.model.KanjiMeaning;
import org.landon.kanji.model.KanjiRadicals;
import org.landon.kanji.repository.MeaningRepository;
import org.landon.kanji.repository.RadicalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value="/ref")
public class KanjiReferences {

    @Autowired
    private MeaningRepository meaningRepo;

    public KanjiReferences(){

    }

    @GetMapping(value = "*")
    public @ResponseBody String getKanjiFallback(){
        return "\u001b[31mError:\u001b[0m Only /kanji/{char here} is a valid request.\n";
    }

    @GetMapping(value = "/{kanji}")
    public @ResponseBody String getKanji(@PathVariable(value = "kanji", required = true) String kanji){
        KanjiMeaning k = meaningRepo.findMeaningByLiteral(kanji);

        // Cannot find, return error.
        if(k == null)
            return "\u001b[31mError:\u001b[0m Character \"" + kanji + "\" not found.\n";

        //Data from meaning
        String literal = k.getLiteral();
        Map<String, String> dicNumber = k.getDic_number();

        String nanori = k.getNanori();
        String temp;

        //Return message to console.
        String out = "\t\t" + k.getLiteral() + "   (" + k + " strokes)\n";
        out += "Radicals:\n\t" + "\n";

        return out;
    }

    //@CrossOrigin()
    @GetMapping(value = "/{kanji}", produces = {"application/json", "application/xml"})
    public @ResponseBody
    Map<String,String> getKanjiApplication(@PathVariable(value = "kanji", required = true) String kanji){
        return meaningRepo.findMeaningByLiteral(kanji).getDic_number();
    }

    @GetMapping(value = "/{kanji}", produces = {"text/html"})
    public String getKanjiHTML(@PathVariable(value = "kanji", required = true) String kanji, Model model) {
        model.addAttribute("meaning", meaningRepo.findMeaningByLiteral(kanji));
        return "references.html";
    }
}
