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

}
