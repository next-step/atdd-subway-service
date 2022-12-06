package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    private final Line line = new Line.Builder().name("1호선").color("red").build();
    private final Station stationA = Station.from("수원역");
    private final Station stationB = Station.from("성균관대역");
    private final Station stationC = Station.from("화서역");
    private final Station stationD = Station.from("세류역");
    private final Distance distance = Distance.from(10);

    @Test
    @DisplayName("하행역을 변경시킨다.")
    void updateDownStation() {
        Section makeSection = Section.of(line, stationA, stationB, distance);

        makeSection.updateDownStation(stationC, 5);
        assertThat(makeSection.getDownStation()).isEqualTo(stationC);
    }


    @Test
    @DisplayName("상행역을 변경시킨다..")
    void updateUpStation() {
        Section makeSection = Section.of(line, stationA, stationB, distance);

        makeSection.updateUpStation(stationD, 5);
        assertThat(makeSection.getUpStation()).isEqualTo(stationD);
    }

    @Test
    @DisplayName("구간 간의 거리를 더한다")
    void plusDistance() {
        Section makeSectionA = Section.of(line, stationA, stationB, distance);
        Section makeSectionB = Section.of(line, stationA, stationB, distance);
        Distance result = makeSectionA.plusDistance(makeSectionB);
        assertThat(result.getDistance()).isEqualTo(20);
    }

    @Test
    @DisplayName("같은 하행역인지 비교한다")
    void isSameDownStation() {
        Section makeSection = Section.of(line, stationA, stationB, distance);
        assertThat(makeSection.isSameDownStation(stationB)).isTrue();
    }

    @Test
    @DisplayName("같은 상행역인지 비교한다.")
    void isSameUpStation() {
        Section makeSection = Section.of(line, stationA, stationB, distance);
        assertThat(makeSection.isSameUpStation(stationA)).isTrue();
    }
}