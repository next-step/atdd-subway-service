package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {
    Station station0;
    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Line line;
    Section section;
    Distance distance;

    @BeforeEach
    void setUp() {
        station0 = new Station("서초역");
        station1 = new Station("강남역");
        station2 = new Station("력삼역");
        station3 = new Station("선릉역");
        station4 = new Station("삼성역");
        line = new Line("2호선", "bg-100", station1, station3, 100);
        section = new Section(line, station1, station3, new Distance(50));
        distance = new Distance(10);
    }

    @Test
    @DisplayName("상행역과 거리를 업데이트 한다")
    void updateUpStation() {
        section.updateUpStation(station2, distance);
        assertThat(section.getUpStation()).isEqualTo(station2);
        assertThat(section.getDistance()).isEqualTo(new Distance(40));
    }

    @Test
    @DisplayName("하행역과 거리를 업데이트 한다")
    void updateDownStation() {
        section.updateDownStation(station2, distance);
        assertThat(section.getDownStation()).isEqualTo(station2);
        assertThat(section.getDistance()).isEqualTo(new Distance(40));
    }

    @Test
    @DisplayName("구간 거리를 더한값을 반환한다")
    void plusDistance() {
        Section section = new Section(line, station3, station4, new Distance(50));
        assertThat(this.section.plusDistance(section)).isEqualTo(new Distance(100));
    }
}
