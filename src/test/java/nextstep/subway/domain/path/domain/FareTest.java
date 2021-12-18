package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Distance;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("요금 계산")
class FareTest {

    @Test
    @DisplayName("기본 운임")
    void baseFare() {
        final Fare fare = new Fare(new Distance(10));
        Assertions.assertThat(fare).isEqualTo(new Fare(1250));
    }

    @ParameterizedTest
    @CsvSource(value = {"15:1350","50:2050", "58:2150", "106:2750"}, delimiter = ':')
    @DisplayName("이용거리 초과에 따른 추가 운임")
    void exceededDistance(int distance, int fareAmount) {
        final Fare fare = new Fare(new Distance(distance));
        Assertions.assertThat(fare).isEqualTo(new Fare(fareAmount));
    }

    @Test
    @DisplayName("기본 운임 미만일 시")
    void baseFareLessThanThat() {
        assertThatIllegalArgumentException().isThrownBy(() ->
            new Fare(1000)
        );
    }
}