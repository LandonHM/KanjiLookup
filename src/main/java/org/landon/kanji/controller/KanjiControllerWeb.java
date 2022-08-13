package org.landon.kanji.controller;

import org.landon.kanji.model.Input;
import org.landon.kanji.model.KanjiMeaning;
import org.landon.kanji.repository.MeaningRepository;
import org.landon.kanji.repository.RadicalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * Class which handles the request for text/html (usually web browsers)
 */
@Controller
@RequestMapping(value="/")
public class KanjiControllerWeb {

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

    public KanjiControllerWeb() {

    }

    /**
     * Default mapping, returns intro page and allows for user input
     * @param model Model spring uses to pass data to thymeleaf
     * @return default web entrance
     */
    @GetMapping(value = "*", produces = {"text/html"})
    public String getKanji(Model model) {
        //System.out.println("\n--RAW CALLED--\n");
        model.addAttribute("input", new Input());
        return "index.html";
    }

    @GetMapping(value={"/error", "/Error"})
    public String getError(Model model){
        model.addAttribute("input", new Input());
        return "error.html";
    }

    @GetMapping(value = "/error/{kanji}")
    public String geterrorKanji(@PathVariable(value = "kanji") String kanji, Model model) {
        model.addAttribute("badString", kanji);
        model.addAttribute("input", new Input());
        return "error_kanji.html";
    }

    @GetMapping(value = "/Error/{kanji}")
    public String getErrorKanji(@PathVariable(value = "kanji") String kanji, Model model) {
        model.addAttribute("badString", kanji);
        model.addAttribute("input", new Input());
        return "error.html";
    }

    /**
     * Gets and passes data relating to the kanji which was searched to the webpage
     * @param search String representation of kanji character which the user want to search
     * @param model Model spring uses to pass data to thymeleaf
     * @return results page for a specific kanji
     */
    @GetMapping(value = "/{search}", produces = {"text/html"})
    public String getKanji(@PathVariable(value = "search") String search, Model model) {
        //System.out.println("{kanji} called");
        // The svg files are stored as the characters hex value padded with 0's
        model.addAttribute("input", new Input());

        if(!isEnglish(search)) {
            String fileName = String.format("%1$05x.svg", (int) search.charAt(0));
            model.addAttribute("search", search);
            model.addAttribute("filename", fileName);
            model.addAttribute("meaning", meaningRepo.findMeaningByLiteral(search));
            model.addAttribute("radicals", radicalRepo.findRadicalsByKanji(search));
            return "result.html";
        }else {
            model.addAttribute("search", search);
            // Get results for english meaning
            KanjiMeaning[] results = meaningRepo.findMeaningByEnMeaning(search);
            // Sort by frequency
            // If both don't have frequency compare stoke counts
            Arrays.sort(results, (a, b) -> {
                int i = Integer.parseInt(a.getMisc().getOrDefault("freq", String.valueOf(Integer.parseInt(a.getMisc().get("stroke_count")) + 2501)));
                int j = Integer.parseInt(b.getMisc().getOrDefault("freq", String.valueOf(Integer.parseInt(b.getMisc().get("stroke_count")) + 2501)));
                return Integer.compare(i,j);
            });
            model.addAttribute("results", results);
            return "en_result.html";
        }
    }

    /**
     * Gets called when user enters input or clicks search
     * @param in Input object which is represents the users input in the form (search bar)
     * @return Redirects user to url with encoded character
     */
    @PostMapping(value = "*", produces = {"text/html"})
    public String getKanji(@ModelAttribute Input in) {
        //System.out.println("\n--MODEL ATTRIBUTE CALLED--\n");
        // Just take the first character and ignore the rest
        String input = in.getKanji();

        if(input == null)
            return "redirect:/";

        if(isEnglish(input)){
            return "redirect:/" + input;
        }else{
            return "redirect:/"+ URLEncoder.encode(input.substring(0,1), StandardCharsets.UTF_8);
        }
    }

    @GetMapping(value = "/sources", produces = {"text/html"})
    public String getSources(Model model) {
        return "sources.html";
    }

    private boolean isEnglish(String s){
        for(char c : s.toCharArray())
            if((int) c > 128)
                return false;
        return true;
    }

}