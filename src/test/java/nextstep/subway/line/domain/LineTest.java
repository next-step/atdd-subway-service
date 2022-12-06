package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    private final Line line = new Line.Builder().name("1호선").color("red").build();
    private final Station stationA = Station.from("수원역");
    private final Station stationB = Station.from("성균관대역");
    private final Station stationC = Station.from("화서역");
    private final Station stationD = Station.from("세류역");

    @Test
    @DisplayName("노선에서 역을 조회한다")
    void getLine() {
        line.addSection(stationA, stationB, 10);
        line.addSection(stationB, stationC, 10);
        line.addSection(stationC, stationD, 10);

        assertThat(line.getStations()).containsOnly(stationA, stationB, stationC, stationD);
        assertThat(line.getStations()).hasSize(4);
    }

    @Test
    @DisplayName("노선에서 역을 삭제한다")
    void removeLine() {
        line.addSection(stationA, stationB, 10);
        line.addSection(stationB, stationC, 10);
        line.addSection(stationC, stationD, 10);

        line.removeLineStation(stationB);

        assertThat(line.getSections()).hasSize(2);
    }
}