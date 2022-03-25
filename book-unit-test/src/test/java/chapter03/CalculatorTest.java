package chapter03;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void sum() {
        // given
        double first = 10;
        double second = 20;
        Calculator calculator = new Calculator();

        // when
        double result = calculator.sum(first, second);

        // then
        assertThat(result).isEqualTo(30);
    }
}