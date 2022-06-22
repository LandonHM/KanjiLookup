package org.landon.kanji.controller;

import java.util.Map;

/**
 * Static class which maps shortened dictionary titles to their proper form as defined from the source (EDRDG kanjidic2)
 */
public class Dictionary {
    public static final Map<String, String> map = Map.ofEntries(
            Map.entry("nelson_c", "Modern Reader's Japanese-English Character Dictionary"),
            Map.entry("nelson_n", "The New Nelson Japanese-English Character Dictionary"),
            Map.entry("halpern_njecd", "New Japanese-English Character Dictionary"),
            Map.entry("halpern_kkd", "Kodansha Kanji Dictionary, (2nd Ed. of the NJECD)"),
            Map.entry("halpern_kkld", "Kanji Learners Dictionary (Kodansha)"),
            Map.entry("halpern_kkld_2ed", "Kanji Learners Dictionary (Kodansha), 2nd ed"),
            Map.entry("heisig", "Remembering The  Kanji  by  James Heisig"),
            Map.entry("heisig6", "Remembering The  Kanji, Sixth Ed.  by  James Heisig"),
            Map.entry("gakken", "A  New Dictionary of Kanji Usage (Gakken)"),
            Map.entry("oneill_names", "Japanese Names (P.G. O'Neill)"),
            Map.entry("oneill_kk", "Essential Kanji (P.G. O'Neill)"),
            Map.entry("moro", "Daikanwajiten"),
            Map.entry("henshall", "A Guide To Remembering Japanese Characters"),
            Map.entry("sh_kk", "Kanji and Kana"),
            Map.entry("sh_kk2", "Kanji and Kana (2011 edition)"),
            Map.entry("sakade", "A Guide To Reading and Writing Japanese"),
            Map.entry("jf_cards", "Japanese Kanji Flashcards (Hodges, Okazaki)"),
            Map.entry("henshall3", "A Guide To Reading and Writing Japanese 3rd edition"),
            Map.entry("tutt_cards", "Tuttle Kanji Cards"),
            Map.entry("crowley", "The Kanji Way to Japanese Language Power"),
            Map.entry("kanji_in_context", "Kanji in Context"),
            Map.entry("busy_people", "Japanese For Busy People"),
            Map.entry("kodansha_compact", "Kodansha Compact Kanji Guide"),
            Map.entry("maniette", "Les Kanjis dans la tete"),

            Map.entry("jis208", "JIS X 0208 (kuten coding)"),
            Map.entry("jis212", "JIS X 0212 (kuten coding)"),
            Map.entry("jis213", "JIS X 0213 (kuten coding)"),
            Map.entry("deroo", "De Roo number"),
            Map.entry("njecd", "Halpern NJECD index number"),
            Map.entry("s_h", "The Kanji Dictionary (Spahn & Hadamitzky)"),
            Map.entry("oneill", "Japanese Names (O'Neill)"),
            Map.entry("ucs", "Unicode codepoint (hex)")
    );
}
