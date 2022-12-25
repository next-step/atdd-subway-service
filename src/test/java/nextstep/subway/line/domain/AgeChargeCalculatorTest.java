package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import nextstep.subway.line.application.AgeChargeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AgeChargeCalculatorTest {

    @Test
    @DisplayName("나이 요금 할인 - 해당 없는 나이 19세 이상")
    void calculate1(){
        // given
        int charge = 1250;
        int age = 20;

        // when
        int result = AgeChargeCalculator.calculate(charge, age);

        // then
        assertThat(result).isEqualTo(1250);
    }

    @Test
    @DisplayName("나이 요금 할인 - 청소년 : 13세이상 19세 미만")
    void calculate2(){
        // given
        int charge = 1250;
        int age = 18;

        // when
        int result = AgeChargeCalculator.calculate(charge, age);

        // then
        assertThat(result).isEqualTo(720);
    }

    @Test
    @DisplayName("나이 요금 할인 - 어린이 : 6세이상 13세 미만")
    void calculate3(){
        // given
        int charge = 1250;
        int age = 12;

        // when
        int result = AgeChargeCalculator.calculate(charge, age);

        // then
        assertThat(result).isEqualTo(450);
    }

    @Test
    @DisplayName("나이 요금 할인 - 유아 : 6세 미만")
    void calculate4(){
        // given
        int charge = 1250;
        int age = 5;

        // when
        int result = AgeChargeCalculator.calculate(charge, age);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("나이 요금 할인 - 노인 : 65세 이상")
    void calculate5(){
        // given
        int charge = 1250;
        int age = 65;

        // when
        int result = AgeChargeCalculator.calculate(charge, age);

        // then
        assertThat(result).isEqualTo(0);
    }
}
