package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FareTest {

    @Test
    void of() {
        // Given
        Station 강남역 = new Station("강남역");
        Station 판교역 = new Station("판교역");

        Line line = new Line("신분당선", "RED", 강남역, 판교역, 10, 0);

        // When
        Fare 기본요금 = Fare.of(line.getSections(), new TotalDistance(10), 20);
        Fare 거리_10초과_50이하 = Fare.of(line.getSections(),new TotalDistance(50), 20);
        Fare 거리_50초과 = Fare.of(line.getSections(),new TotalDistance(51), 20);

        // Then
        assertAll(
                () -> assertThat(기본요금.getFare()).isEqualTo(1250),
                () -> assertThat(거리_10초과_50이하.getFare()).isEqualTo(1250 + (int) ((Math.ceil((50 - 1) / 5) + 1) * 100)),
                () -> assertThat(거리_50초과.getFare()).isEqualTo(1250 + (int) ((Math.ceil((51 - 1) / 8) + 1) * 100))
        );
    }
}
