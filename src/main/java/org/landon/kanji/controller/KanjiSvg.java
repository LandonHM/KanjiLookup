package org.landon.kanji.controller;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.server.ExportException;

@Controller
@RequestMapping(value="/svg")
public class KanjiSvg {

    public KanjiSvg(){

    }

    @GetMapping(value = "*")
    public @ResponseBody String getKanjiFallback(){
        return "\u001b[31mError:\u001b[0m Only /kanji/{char here} is a valid request.\n";
    }

    @GetMapping(value = "/{kanji}")
    public ResponseEntity<Resource> getKanjiHTML(@PathVariable(value = "kanji", required = true) String kanji) {
        String hex = Integer.toHexString(kanji.charAt(0));
        String fileName = String.format("%1$5s",hex).replace(' ', '0')+".svg";
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

    @GetMapping(value = "*", produces = {"text/html"})
    public String getKanjiHTML(Model model) {
        return "svg.html";
    }

    @GetMapping(value = "/{kanji}", produces = {"text/html"})
    public String getKanjiHTML(@PathVariable(value = "kanji", required = true) String kanji, Model model) {
        String hex = Integer.toHexString(kanji.charAt(0));
        String fileName = String.format("%1$5s",hex).replace(' ', '0')+".svg";

        model.addAttribute("kanji", fileName);
        return "svg.html";
    }
}
