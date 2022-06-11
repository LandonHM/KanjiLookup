package org.landon.kanji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class KanjiLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanjiLookupApplication.class, args);
	}

	@GetMapping(value= "/")
	public String mainPage(){
		return "index.html";
	}

	@GetMapping(value = "/help", produces = {"text/html"})
	public String getHelpHTML(){
		return "help.html";
	}

	@GetMapping(value="/help")
	public @ResponseBody String getHelpStr(){
		return "Here is help";
	}
}
