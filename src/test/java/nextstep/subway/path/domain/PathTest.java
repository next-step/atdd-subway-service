package nextstep.subway.path.domain;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathTest {
    private List<Station> stations;

    @BeforeEach
    void setUp() {
        stations = Arrays.asList(new Station("강남역"), new Station("역삼역"));
    }

    @DisplayName("빈 지하철 목록으로 경로 생성 시 예외가 발생한다.")
    @Test
    void createPathWithEmptyStations() {
        assertThatThrownBy(() -> new Path(Collections.emptyList(), 10, new Fare(500)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로는 비어있을 수 없습니다.");
    }

    @DisplayName("어린이일 경우 요금에서 350원을 차감한 금액의 50%가 할인된다.")
    @Test
    void fareWithKids() {
        Path path = new Path(stations, 10, new Fare(0));

        Fare actualFare = path.calculateTotalFare(10);

        assertThat(actualFare.value()).isEqualTo(450);
    }

    @DisplayName("청소년일 경우 요금에서 350원을 차감한 금액의 80%가 할인된다.")
    @Test
    void fareWithTeenager() {
        Path path = new Path(stations, 10, new Fare(0));

        Fare actualFare = path.calculateTotalFare(18);

        assertThat(actualFare.value()).isEqualTo(720);
    }

    @DisplayName("성인일 경우 요금이 할인되지 않는다.")
    @Test
    void fareWithAdult() {
        Path path = new Path(stations, 10, new Fare(0));

        Fare actualFare = path.calculateTotalFare(26);

        assertThat(actualFare.value()).isEqualTo(1250);
    }

    @DisplayName("정해진 거리정책에 따라 거리별 요금이 적용된다.")
    @ParameterizedTest
    @CsvSource(value = {"10:1250", "11:1350", "50:2050", "51:2150", "100:2750"}, delimiter = ':')
    void fareWithDistance(int distance, int expectedFare) {
        Path path = new Path(stations, distance, new Fare(0));

        Fare actualFare = path.calculateTotalFare(26);

        assertThat(actualFare.value()).isEqualTo(expectedFare);
    }

    @DisplayName("노선의 추가요금이 존재 할 경우 추가요금이 기본 요금에 더해진다.")
    @Test
    void fareWithLineExtraFare() {
        Path path = new Path(stations, 10, new Fare(500));

        Fare actualFare = path.calculateTotalFare(26);

        assertThat(actualFare.value()).isEqualTo(1750);
    }
}
