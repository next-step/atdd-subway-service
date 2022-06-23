package nextstep.subway.member.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @ParameterizedTest
    @CsvSource(value = {"5,1250", "6,450", "12,450", "13,720"})
    void 어린이_할인을_받을_수_있는_나이는_6세_이상_13세_미만이다(int ageValue, int expectedFare) {
        Age age = new Age(ageValue);
        assertThat(age.calculateAgeSale(1250)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"12,450", "13,720", "18,720", "19,1250"})
    void 청소년_할인을_받을_수_있는_나이는_13세_이상_19세_미만이다(int ageValue, int expectedFare) {
        Age age = new Age(ageValue);
        assertThat(age.calculateAgeSale(1250)).isEqualTo(expectedFare);
    }
}