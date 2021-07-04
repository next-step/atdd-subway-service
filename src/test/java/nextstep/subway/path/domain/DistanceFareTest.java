package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DistanceFareTest {

    @DisplayName("지하철 최단 경로 거리가 1~10 거리일 때 1250원이 부과된다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void calculateStandardFare(int distance) {
        //when
        int actual = DistanceFare.STANDARD.calculateFare(distance);

        //then
        assertThat(actual).isEqualTo(1250);
    }

    @DisplayName("지하철 최단 경로 거리가 11~50 거리일 때 기본요금과 5km마다 100원이 더해진 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource({
            "11, 1350",
            "16, 1450",
            "21, 1550",
            "26, 1650",
            "31, 1750",
            "36, 1850",
            "41, 1950",
            "46, 2050"
    })
    void calculateFirstExtraFare(int distance, int fare) {
        //when
        int actual = DistanceFare.FIRST_EXTRA.calculateFare(distance);

        //then
        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("지하철 최단 경로 거리가 51 이상일 때 기본요금과 5km마다 100원이 더해진 요금과 8km마다 100원이 더해진 요금이 부과된다.")
    @ParameterizedTest
    @CsvSource({
            "51, 2150",
            "58, 2150",
            "59, 2250",
            "66, 2250",
            "67, 2350",
            "74, 2350",
            "75, 2450"
    })
    void calculateSecondExtraFare(int distance, int fare) {
        //when
        int actual = DistanceFare.SECOND_EXTRA.calculateFare(distance);

        //then
        assertThat(actual).isEqualTo(fare);
    }

    @DisplayName("최단 경로의 거리가 1~10 사이일시 기본 과금 정책이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void findStandardDistanceFareByDistance(int distance) {
        //when
        DistanceFare actual = DistanceFare.findDistanceFareByDistance(distance);

        //then
        assertThat(actual).isEqualTo(DistanceFare.STANDARD);
    }

    @DisplayName("최단 경로의 거리가 11~51 사이일시 두번째 과금 정책이다.")
    @ParameterizedTest
    @ValueSource(ints = {11, 16, 21, 26, 31, 36, 41, 46})
    void findFirstExtraDistanceFareByDistance(int distance) {
        //when
        DistanceFare actual = DistanceFare.findDistanceFareByDistance(distance);

        //then
        assertThat(actual).isEqualTo(DistanceFare.FIRST_EXTRA);
    }

    @DisplayName("최단 경로의 거리가 51 이상일시 세번째 과금 정책이다.")
    @ParameterizedTest
    @ValueSource(ints = {51, 55, 60, 63, 88, 90, 99, Integer.MAX_VALUE})
    void findSecondExtraDistanceFareByDistance(int distance) {
        //when
        DistanceFare actual = DistanceFare.findDistanceFareByDistance(distance);

        //then
        assertThat(actual).isEqualTo(DistanceFare.SECOND_EXTRA);
    }

    @DisplayName("최단 경로 거리를 0 이하로 입력시 예외가 발생한다.")
    @Test
    void distanceRangeOverException() {
        //when
        assertThatThrownBy(() -> DistanceFare.findDistanceFareByDistance(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DistanceFare.DISTANCE_RANGE_OVER_EXCEPTION_MESSAGE);
    }
}
