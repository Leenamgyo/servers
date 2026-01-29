package com.example.app.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:mysql://127.0.0.1:3306/auth",
    "spring.datasource.username=user",
    "spring.datasource.password=password",
    "spring.jpa.hibernate.ddl-auto=none",
    "spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect"
})
class DataSourceTest {

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("DB 연결(Connection) 획득 테스트")
    void testConnection() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.isValid(1)).isTrue();
            
            System.out.println("✅ Database Connection Successful!");
            System.out.println("✅ DB URL: " + connection.getMetaData().getURL());
            System.out.println("✅ Username: " + connection.getMetaData().getUserName());
        }
    }
}