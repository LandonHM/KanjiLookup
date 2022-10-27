package org.landon.kanji;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.jetty.ConfigurableJettyWebServerFactory;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;


import java.awt.*;
import java.util.Collections;

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

@Configuration
class WebServerFactoryCustomizerConfig implements WebServerFactoryCustomizer<ConfigurableJettyWebServerFactory> {
	@Override
	public void customize(ConfigurableJettyWebServerFactory factory) {
		((JettyServletWebServerFactory)factory).setConfigurations(
				Collections.singleton(new HttpToHttpsJettyConfig())
		);

		factory.addServerCustomizers(
				server -> {
					HttpConfiguration httpConfiguration = new HttpConfiguration();
					httpConfiguration.setSecurePort(8443);
					httpConfiguration.setSecureScheme("https");

					ServerConnector connector = new ServerConnector(server);
					connector.addConnectionFactory(new HttpConnectionFactory(httpConfiguration));
					connector.setPort(8080);

					server.addConnector(connector);
				}
		);
	}
}

class HttpToHttpsJettyConfig extends AbstractConfiguration {
	@Override
	public void configure(WebAppContext context) throws Exception {
		Constraint constraint = new Constraint();
		constraint.setDataConstraint(Constraint.DC_CONFIDENTIAL);

		ConstraintMapping mapping = new ConstraintMapping();
		mapping.setPathSpec("/*");
		mapping.setConstraint(constraint);

		ConstraintSecurityHandler handler = new ConstraintSecurityHandler();
		handler.addConstraintMapping(mapping);

		context.setSecurityHandler(handler);
	}
}
