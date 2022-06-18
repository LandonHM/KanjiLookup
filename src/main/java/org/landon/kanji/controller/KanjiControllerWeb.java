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
import java.util.Collections;
import java.util.HashMap;


/**
 * Class which handles the request for text/html (usually web browsers)
 */
@Controller
@RequestMapping(value="/")
public class KanjiControllerWeb {

    /*private static final HashMap<String, String> dictionaryReferences;
    static{
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("nelson_c", "Modern Reader's Japanese-English Character Dictionary");
        tmp.put("nelson_n", "The New Nelson Japanese-English Character Dictionary");
        tmp.put("halpern_njecd", "New Japanese-English Character Dictionary");
        tmp.put("halpern_kkd", "Kodansha Kanji Dictionary, (2nd Ed. of the NJECD)");
        tmp.put("halpern_kkld", "Kanji Learners Dictionary (Kodansha)");
        tmp.put("halpern_kkld_2ed", "Kanji Learners Dictionary (Kodansha), 2nd ed");
        tmp.put("heisig", "Remembering The  Kanji  by  James Heisig");
        tmp.put("heisig6", "Remembering The  Kanji, Sixth Ed.  by  James Heisig");
        tmp.put("gakken", "A  New Dictionary of Kanji Usage (Gakken)");
        tmp.put("oneill_names", "Japanese Names (P.G. O'Neill)");
        tmp.put("oneill_kk", "Essential Kanji (P.G. O'Neill)");
        tmp.put("moro", "Daikanwajiten");
        tmp.put("henshall", "A Guide To Remembering Japanese Characters");
        tmp.put("sh_kk", "Kanji and Kana");
        tmp.put("sh_kk2", "Kanji and Kana (2011 edition)");
        tmp.put("sakade", "A Guide To Reading and Writing Japanese");
        tmp.put("jf_cards", "Japanese Kanji Flashcards (Max Hodges and Tomoko Okazaki)");
        tmp.put("henshall3", "A Guide To Reading and Writing Japanese 3rd edition");
        tmp.put("tutt_cards", "Tuttle Kanji Cards");
        tmp.put("crowley", "The Kanji Way to Japanese Language Power");
        tmp.put("kanji_in_context", "Kanji in Context");
        tmp.put("busy_people", "Japanese For Busy People");
        tmp.put("kodansha_compact", "Kodansha Compact Kanji Guide");
        tmp.put("maniette", "Les Kanjis dans la tete");

        tmp.put("jis208", "JIS X 0208 (kuten coding)");
        tmp.put("jis212", "JIS X 0212 (kuten coding)");
        tmp.put("jis213", "JIS X 0213 (kuten coding)");
        tmp.put("deroo", "De Roo number");
        tmp.put("njecd", "Halpern NJECD index number");
        tmp.put("s_h", "The Kanji Dictionary (Spahn & Hadamitzky)");
        tmp.put("nelson_c", "\"Classic\" Nelson");
        tmp.put("oneill", "Japanese Names (O'Neill)");
        tmp.put("ucs", "Unicode codepoint (hex)");

        dictionaryReferences = (HashMap<String, String>) Collections.unmodifiableMap(tmp);
    }*/

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
    public String getKanji(Model model) {
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
    public String getKanji(@PathVariable(value = "kanji", required = true) String kanji, Model model) {
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
    public String getKanji(@ModelAttribute Input in, Model model) {
        //System.out.println("\n--MODEL ATTRIBUTE CALLED--\n");
        try {
            // Just take the first character and ignore the rest
            return "redirect:/"+ URLEncoder.encode(in.getKanji().substring(0,1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "redirect:/";
        }
    }

    @GetMapping(value = "/sources", produces = {"text/html"})
    public String getSources(Model model) {
        return "sources.html";
    }

}