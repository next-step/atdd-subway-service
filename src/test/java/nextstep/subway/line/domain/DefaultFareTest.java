package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultFareTest {

    @DisplayName("성인일때 기본운임 구하기")
    @CsvSource(value = {"19:1250", "50:1250"}, delimiter = ':')
    @ParameterizedTest
    void adultDefaultFare(int age, int fare) {
        DefaultFare defaultFare = DefaultFare.of(age);
        assertThat(defaultFare.fare()).isEqualTo(fare);
    }

    @DisplayName("청소년일때 기본운임 구하기")
    @CsvSource(value = {"13:720", "18:720"}, delimiter = ':')
    @ParameterizedTest
    void teenagerDefaultFare(int age, int fare) {
        DefaultFare defaultFare = DefaultFare.of(age);
        assertThat(defaultFare.fare()).isEqualTo(fare);
    }

    @DisplayName("어린이일때 기본운임 구하기")
    @CsvSource(value = {"6:450", "12:450"}, delimiter = ':')
    @ParameterizedTest
    void childDefaultFare(int age, int fare) {
        DefaultFare defaultFare = DefaultFare.of(age);
        assertThat(defaultFare.fare()).isEqualTo(fare);
    }

    @DisplayName("나이 정보가 없을때 구하기")
    @Test
    void ageIsNullDefaultFare() {
        DefaultFare defaultFare = DefaultFare.of(null);
        assertThat(defaultFare.fare()).isEqualTo(1250);
    }
}
