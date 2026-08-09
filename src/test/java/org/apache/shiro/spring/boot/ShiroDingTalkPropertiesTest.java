package org.apache.shiro.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ShiroDingTalkProperties Tests")
class ShiroDingTalkPropertiesTest {
    @Test
    @DisplayName("Default constructor creates non-null instance")
    void testDefaultInstance() {
        ShiroDingTalkProperties props = new ShiroDingTalkProperties();
        assertThat(props).isNotNull();
    }

    @Test
    @DisplayName("PREFIX constant has expected value")
    void testPREFIXConstant() {
        assertThat(ShiroDingTalkProperties.PREFIX).isEqualTo("shiro.dingtalk");
    }

    @Test
    @DisplayName("enabled field can be set via reflection")
    void testEnabledField() throws Exception {
        ShiroDingTalkProperties props = new ShiroDingTalkProperties();
        java.lang.reflect.Field f = ShiroDingTalkProperties.class.getDeclaredField("enabled");
        f.setAccessible(true);
        f.set(props, true);
        assertThat(f.getBoolean(props)).isTrue();
    }

    @Test
    @DisplayName("corpId field can be set via reflection")
    void testCorpIdField() throws Exception {
        ShiroDingTalkProperties props = new ShiroDingTalkProperties();
        java.lang.reflect.Field f = ShiroDingTalkProperties.class.getDeclaredField("corpId");
        f.setAccessible(true);
        f.set(props, "testCorpId");
        assertThat(f.get(props)).isEqualTo("testCorpId");
    }
}
