package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("노선에서 역을 삭제한다")
    void removeLine() {
        Line line = Line.of("1호선", "red");
        Station deleteStation = Station.of("수원역");
        Section sectionA = Section.of(line, deleteStation, Station.of("성균관대역"), Distance.from(10));
        Section sectionB = Section.of(line, Station.of("화서역"), deleteStation, Distance.from(10));
        Section sectionC = Section.of(line, Station.of("화서역"), Station.of("성균관대역"), Distance.from(10));
        line.addSection(sectionA, Distance.from(10));
        line.addSection(sectionB, Distance.from(10));
        line.addSection(sectionC, Distance.from(10));

        line.removeLineStation(deleteStation);

        assertThat(line.getSections()).hasSize(2);
    }
}