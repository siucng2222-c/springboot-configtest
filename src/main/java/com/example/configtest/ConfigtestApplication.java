package com.example.configtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(BootifulProperties.class)
public class ConfigtestApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ConfigtestApplication.class, args);

		new SpringApplicationBuilder().sources(ConfigtestApplication.class)
				// .initializers(applicationContext ->
				// applicationContext.getEnvironment().getPropertySources()
				// .addLast(new BootifulPropertySource())

				// )
				.run(args);
	}

	@Autowired
	void contributeToPropertySources(ConfigurableEnvironment environment) {
		environment.getPropertySources().addLast(new BootifulPropertySource());
	}

	@Bean
	ApplicationRunner applicationRunner(Environment environment, @Value("${HOME}") String homePath,
			@Value("${message-from-program-args:}") String messageFromProgramArgs,
			@Value("${bootiful-message}") String bootifulMessage, BootifulProperties bp,
			@Value("${greeting-message:Default Hello : ${message-from-app-prop}}") String greeting) {
		return args -> {
			log.info("message from application.properties = " + environment.getProperty("message-from-app-prop"));
			log.info("greeting message = " + greeting);
			log.info("Home path = " + homePath);
			log.info("spring.datasource.url = " + environment.getProperty("spring.datasource.url"));
			log.info("message-from-program-args = " + messageFromProgramArgs);
			log.info("message from custom property source = " + bootifulMessage);
			log.info("message from BootifulProperties = " + bp.getMessage());
		};
	}

	static class BootifulPropertySource extends PropertySource<String> {
		public BootifulPropertySource() {
			super("bootiful");
		}

		@Override
		public Object getProperty(String name) {
			if (name.equalsIgnoreCase("bootiful-message")) {
				return "Hello from " + BootifulPropertySource.class.getSimpleName() + " private class!";
			}

			return null;
		}
	}

}

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("bootiful-properties")
class BootifulProperties {
	private String message;
}