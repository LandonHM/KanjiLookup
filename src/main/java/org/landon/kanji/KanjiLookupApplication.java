package org.landon.kanji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.*;

@SpringBootApplication
@Controller
public class KanjiLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanjiLookupApplication.class, args);
	}

	@GetMapping(value = "robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String getRobotsTxt() {
		return "User-agent: *\n" +
				"Disallow:\n";
	}

}
