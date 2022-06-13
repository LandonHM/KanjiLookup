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
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
        return "search.html";
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
        String hex = Integer.toHexString(kanji.charAt(0));
        String fileName = String.format("%1$5s", hex).replace(' ', '0') + ".svg";

        model.addAttribute("kanji", fileName);
        model.addAttribute("meaning", meaningRepo.findMeaningByLiteral(kanji));
        model.addAttribute("radicals", radicalRepo.findRadicalsByKanji(kanji));
        model.addAttribute("input", new Input());

        return "search.html";
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
            return "redirect:/"+ URLEncoder.encode(in.getKanji(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/";
        }
    }

}