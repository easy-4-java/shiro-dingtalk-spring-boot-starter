package org.apache.shiro.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ShiroDingTalkWebAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ShiroDingTalkWebAutoConfiguration Tests")
class ShiroDingTalkWebAutoConfigurationTest {

    @Test
    @DisplayName("Auto-configuration class can be instantiated")
    void testInstantiation() {
        ShiroDingTalkWebAutoConfiguration configuration = new ShiroDingTalkWebAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Configuration class exists and is accessible")
    void testConfigurationClassExists() {
        assertThat(ShiroDingTalkWebAutoConfiguration.class).isNotNull();
    }
}
