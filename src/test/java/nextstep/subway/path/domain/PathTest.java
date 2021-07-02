package nextstep.subway.path.domain;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nextstep.subway.TestFixture.*;
import static nextstep.subway.fare.domain.Fare.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    private List<Station> stations = new ArrayList<>(Arrays.asList(회현역, 명동역, 충무로역));

    @DisplayName("거리에 따라 요금계산 : 기본요금")
    @ParameterizedTest
    @CsvSource({"1, true", "10,true"})
    void 경로거리_1부터_10사이(int distance, boolean result) {
        //Given
        Path 경로 = new Path(stations, distance);
        Fare 예상요금 = new Fare(BASE_FARE);

        //When
        Fare 실제요금 = new Fare(경로.getFare());

        //Then
        assertThat(실제요금.equals(예상요금)).isEqualTo(result);
    }

    @DisplayName("거리에 따라 요금계산 : 기본요금 + 5km마다 100원")
    @Test
    void 경로거리_11부터_50사이() {
        //Given
        Path 경로 = new Path(stations, 15);
        Fare 예상요금 = new Fare(BASE_FARE + 100);

        //When
        Fare 실제요금 = new Fare(경로.getFare());

        //Then
        assertThat(실제요금).isEqualTo(예상요금);
    }

    @DisplayName("거리에 따라 요금계산 : 기본요금 + 8km마다 100원")
    @Test
    void 경로거리_51이상() {

        //Given
        Path 경로 = new Path(stations, 58);
        Fare 예상요금 = new Fare(BASE_FARE +
                ((50 - 10) / DISTANCE_FIRST_INTERVAL_DIVIDER) * DISTANCE_EXTRA_CHARGE +
                ((58 - 50) / DISTANCE_SECOND_INTERVAL_DIVIDER) * DISTANCE_EXTRA_CHARGE);

        //When
        Fare 실제요금 = new Fare(경로.getFare());

        //Then
        assertThat(실제요금.getFare()).isEqualTo(예상요금.getFare());
    }
}
