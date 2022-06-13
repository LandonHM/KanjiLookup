package org.landon.kanji.controller;

import org.landon.kanji.model.Input;
import org.landon.kanji.repository.MeaningRepository;
import org.landon.kanji.repository.RadicalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


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
     * @return
     */
    @GetMapping(value = "*", produces = {"text/html"})
    public String getKanjiHTML(Model model) {
        //System.out.println("\n--RAW CALLED--\n");
        model.addAttribute("input", new Input());
        return "index.html";
    }

    /**
     * Gets and passes data relating to the kanji which was searched to the webpage
     * @param kanji String representation of kanji character which the user want to search
     * @param model Model spring uses to pass data to thymeleaf
     * @return
     */
    @GetMapping(value = "/{kanji}", produces = {"text/html"})
    public String getKanjiHTML(@PathVariable(value = "kanji", required = true) String kanji, Model model) {
        //System.out.println("{kanji} called");
        // The svg files are stored as the characters hex value padded with 0's
        String hex = Integer.toHexString(kanji.charAt(0));
        String fileName = String.format("%1$5s", hex).replace(' ', '0') + ".svg";

        model.addAttribute("kanji", kanji);
        model.addAttribute("filename", fileName);
        model.addAttribute("meaning", meaningRepo.findMeaningByLiteral(kanji));
        model.addAttribute("radicals", radicalRepo.findRadicalsByKanji(kanji));
        model.addAttribute("input", new Input());

        return "result.html";
    }

    /**
     * Gets called when user enters input or clicks search
     * @param in Input object which is represents the users input in the form (search bar)
     * @param model Model spring uses to pass data to thymeleaf
     * @return Redirects user to url with encoded character
     */
    @PostMapping(value = "*", produces = {"text/html"})
    public String getKanjiHTML(@ModelAttribute Input in, Model model) {
        //System.out.println("\n--MODEL ATTRIBUTE CALLED--\n");
        try {
            // Just take the first character and ignore the rest
            return "redirect:/"+ URLEncoder.encode(in.getKanji().substring(0,1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/";
        }
    }

}