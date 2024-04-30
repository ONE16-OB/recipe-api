package be.one16.cerbainterview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
@ContextConfiguration(initializers = AbstractBaseTest.DataSourceInitializer.class)
public abstract class AbstractBaseTest {

    private static final PostgreSQLContainer<?> DATABASE;

    static {
        DATABASE = new PostgreSQLContainer<>("postgres:13.4");
        DATABASE.start();
    }

    @Test
    void containerIsRunning() {
        assertThat(DATABASE.isCreated()).isTrue();
        assertThat(DATABASE.isRunning()).isTrue();
    }

    public static class DataSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + DATABASE.getJdbcUrl(),
                    "spring.datasource.username=" + DATABASE.getUsername(),
                    "spring.datasource.password=" + DATABASE.getPassword()
            );
        }
    }
}

