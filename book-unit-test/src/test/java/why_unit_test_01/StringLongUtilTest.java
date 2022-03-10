package why_unit_test_01;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StringLongUtilTest {

    @Test
    void isStringLong() {
        boolean result = StringLongUtil.isStringLong("abc");
        assertThat(result).isFalse();
    }

    @Test
    void isStringLongV2() {
        boolean result = StringLongUtil.isStringLongV2("abc");
        assertThat(result).isFalse();
    }

    @Test
    void isStringLongV2_2() {
        boolean result1 = StringLongUtil.isStringLongV2("abc"); // true
        boolean result2 = StringLongUtil.isStringLongV2("acdef"); // false
    }

    @Test
    void Integer_Parse() {
        String integer = "123";
        int result = StringLongUtil.parse(integer);
        assertThat(result).isEqualTo(123);
    }
}