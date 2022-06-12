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

@Controller
@RequestMapping(value="/")
public class KanjiControllerWeb {

    @Autowired
    private MeaningRepository meaningRepo;

    @Autowired
    private RadicalsRepository radicalRepo;

    public KanjiControllerWeb() {

    }

    @GetMapping(value = "*", produces = {"text/html"})
    public String getKanjiHTML(Model model) {
        System.out.println("\n--RAW CALLED--\n");
        model.addAttribute("input", new Input());
        return "search.html";
    }

    @GetMapping(value = "/{kanji}", produces = {"text/html"})
    public String getKanjiHTML(@PathVariable(value = "kanji", required = true) String kanji, Model model) {

        System.out.println("{kanji} called");

        String hex = Integer.toHexString(kanji.charAt(0));
        String fileName = String.format("%1$5s", hex).replace(' ', '0') + ".svg";

        model.addAttribute("kanji", fileName);
        model.addAttribute("meaning", meaningRepo.findMeaningByLiteral(kanji));
        model.addAttribute("radicals", radicalRepo.findRadicalsByKanji(kanji));
        model.addAttribute("input", new Input());

        return "search.html";
    }

    @PostMapping(value = "*", produces = {"text/html"})
    public String getKanjiHTML(@ModelAttribute Input t, Model model) {
        System.out.println("\n--MODEL ATTRIBUTE CALLED--\n");

        try {
            return "redirect:/"+ URLEncoder.encode(t.getKanji(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/";
        }
    }

}