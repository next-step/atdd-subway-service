package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    private Line testLine;
    private Station testUpStation;
    private Station testDownStation;

    @BeforeEach
    void makeDefaultLine() {
        testUpStation = new Station("서울역");
        testDownStation = new Station("금정역");

        testLine = new Line("1호선", "navy", 0, testUpStation, testDownStation, 10);
    }

    @DisplayName("노선내의 역 목록 조회")
    @Test
    void getStations() {
        assertThat(testLine.getStations()).containsExactly(testUpStation, testDownStation);
    }

    @DisplayName("노선 상에 중간역 추가")
    @Test
    void addMidStation() {
        Station newMidStation = new Station("안양역");
        Section section = new Section(newMidStation, testDownStation, 7);
        testLine.addSection(section);

        assertThat(testLine.getStations()).containsExactly(testUpStation, newMidStation, testDownStation);
    }

}
