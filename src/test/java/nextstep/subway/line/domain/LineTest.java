package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class LineTest {

    @DisplayName("주어진 노선을 새로운 노선정보로 갱신한다")
    @Test
    void update() {
        Line line = new Line("2호선", "green");
        Line updateLine = new Line("1호선", "blue");

        line.update(updateLine);

        assertThat(line).extracting("name").isSameAs("1호선");
        assertThat(line).extracting("color").isSameAs("blue");
    }

    @DisplayName("주어진 노선에 새로운 역들을 구간으로 추가하면 역이 추가된다")
    @Test
    void addSection() {
        Line line = new Line("2호선", "green");
        Station station1 = new Station("강남역");
        Station station2 = new Station("교대역");
        int distance = 10;

        line.addSection(station1, station2, distance);

        assertThat(line.getStations()).containsExactly(station1, station2);
    }

    @DisplayName("주어진 노선에 삭제할 역을 입력하면 목록에서 역이 사라진다.")
    @Test
    void removeSection() {
        Line line = new Line("2호선", "green");
        Station station1 = new Station("강남역");
        Station station2 = new Station("교대역");
        Station station3 = new Station("잠실역");
        int distance1 = 10;
        int distance2 = 5;
        line.addSection(station1, station2, distance1);
        line.addSection(station2, station3, distance2);

        line.removeSection(station1);

        assertThat(line.getStations()).containsExactly(station2, station3);
    }
}
