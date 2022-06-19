package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    private Line testLine;
    private Station testUpStation;
    private Station testDownStation;

    @BeforeEach
    void makeDefaultLine() {
        testUpStation = new Station("서울역");
        testDownStation = new Station("금정역");

        testLine = new Line("1호선", "navy", testUpStation, testDownStation, 10);
    }

    @DisplayName("노선내의 역 목록 조회")
    @Test
    void getStations() {
        Station newDownStation = new Station("수원역");
        testLine.addStation(testDownStation, newDownStation, 7);

        assertThat(testLine.getStations()).containsExactly(testUpStation, testDownStation);
    }

    @DisplayName("노선 상에 상행종점역 추가")
    @Test
    void addUpStation() {
        Station newUpStation = new Station("의정부");
        testLine.addStation(newUpStation, testUpStation, 7);

        assertThat(testLine.getStations()).containsExactly(newUpStation, testUpStation, testDownStation);
    }

    @DisplayName("노선 상에 하행종점역 추가")
    @Test
    void addDownStation() {
        Station newDownStation = new Station("수원역");
        testLine.addStation(testDownStation, newDownStation, 7);

        assertThat(testLine.getStations()).containsExactly(testUpStation, testDownStation, newDownStation);
    }

    @DisplayName("노선 상에 중간역 추가")
    @Test
    void addMidStation() {
        Station newMidStation = new Station("안양역");
        testLine.addStation(newMidStation, testDownStation, 7);

        assertThat(testLine.getStations()).containsExactly(testUpStation, newMidStation, testDownStation);
    }

    @DisplayName("노선 상에 중간 역 삽입시 기존 구간보다 긴 거리를 삽입")
    @Test
    void addOverDistanceStation() {
        Station newMidStation = new Station("안양역");
        testLine.addStation(newMidStation, testDownStation, 15);

        assertThatThrownBy(() -> testLine.addStation(newMidStation, testDownStation, 15))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선 상의 역 제거")
    @Test
    void removeLineStation() {
        Station newDownStation = new Station("수원역");
        testLine.addStation(testDownStation, newDownStation, 7);
        testLine.removeStation(testUpStation);

        assertThat(testLine.getStations()).containsExactly(testDownStation, newDownStation);
    }

    @DisplayName("노선 상에 존재하지 않는 역 제거")
    @Test
    void removeNotExistsStation() {
        Station otherStation = new Station("수원역");

        assertThatThrownBy(() -> testLine.removeStation(otherStation)).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("노선 상행종점 조회")
    @Test
    void findUpStation() {
        assertThat(testLine.findUpStation()).isEqualTo(testUpStation);
    }
}
