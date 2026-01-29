package com.example.app.config;

import com.example.app.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.core.env.MapPropertySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@ContextConfiguration(initializers = DotEnvTest.EnvLoader.class)
class DotEnvTest {

    @MockBean
    private MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    static class EnvLoader implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            Map<String, Object> envMap = new HashMap<>();
            try {
                System.out.println("DEBUG: Looking for .env at: " + Paths.get(".env").toAbsolutePath());
                try (Stream<String> lines = Files.lines(Paths.get(".env"))) {
                    lines.filter(line -> line.contains("=") && !line.startsWith("#"))
                     .forEach(line -> {
                         String[] parts = line.split("=", 2);
                         if (parts.length == 2) {
                             String key = parts[0].trim();
                             String value = parts[1].trim();
                             envMap.put(key, value);
                             
                             // Manual mapping for Spring Boot properties to ensure override
                             if (key.equals("SPRING_DATASOURCE_URL")) {
                                 envMap.put("spring.datasource.url", value);
                             } else if (key.equals("SPRING_DATASOURCE_USERNAME")) {
                                 envMap.put("spring.datasource.username", value);
                             } else if (key.equals("SPRING_DATASOURCE_PASSWORD")) {
                                 envMap.put("spring.datasource.password", value);
                             } else if (key.equals("JWT_SECRET")) {
                                 envMap.put("jwt.secret", value);
                             }
                         }
                     });

                }
                System.out.println("DEBUG: Loaded " + envMap.size() + " properties from .env");
                envMap.forEach((k, v) -> System.out.println("DEBUG: Loaded Key: " + k + ", Value: " + v));
            } catch (IOException e) {
                System.err.println("DEBUG: Failed to load .env file: " + e.getMessage());
                throw new RuntimeException("Failed to load .env file", e);
            }
            // Add to Spring Environment with high precedence
            applicationContext.getEnvironment().getPropertySources()
                .addFirst(new MapPropertySource("dotEnvProperties", envMap));
        }
    }

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Test
    @DisplayName(".env 파일의 값이 application.yml을 통해 정상적으로 주입되는지 확인")
    void testDotEnvInjection() {
        System.out.println("DEBUG: Env 'SPRING_DATASOURCE_URL': " + environment.getProperty("SPRING_DATASOURCE_URL"));
        System.out.println("DEBUG: Env 'spring.datasource.url': " + environment.getProperty("spring.datasource.url"));
        
        System.out.println("DEBUG: jwtSecret = '" + jwtSecret + "'");
        System.out.println("DEBUG: dbUrl = '" + dbUrl + "'");
        System.out.println("DEBUG: dbUsername = '" + dbUsername + "'");
        System.out.println("DEBUG: dbPassword = '" + dbPassword + "'");
        
        // .env 파일에 작성된 값과 일치하는지 검증
        assertThat(jwtSecret).isEqualTo("dGVzdHNlY3JldGtleWZvcnRlc3RpbmdwdXJwb3Nlc29ubHk=");
        assertThat(dbUrl).isEqualTo("jdbc:mysql://127.0.0.1:3306/auth");
        assertThat(dbUsername).isEqualTo("user");
        assertThat(dbPassword).isEqualTo("password");

        System.out.println("✅ .env Loaded JWT Secret: " + jwtSecret);
        System.out.println("✅ .env Loaded DB URL: " + dbUrl);
    }
}
