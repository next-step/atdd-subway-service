package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("노선에서 역을 삭제한다")
    void removeLine() {
        Line line = new Line("1호선", "red");
        Station deleteStation = new Station("수원역");
        Section sectionA = new Section(line, deleteStation, new Station("성균관대역"), Distance.from(20));
        Section sectionB = new Section(line, new Station("화서역"), deleteStation, Distance.from(20));
        Section sectionC = new Section(line, new Station("화서역"), new Station("성균관대역"), Distance.from(20));
        line.addSection(sectionA);
        line.addSection(sectionB);
        line.addSection(sectionC);

        line.removeLineStation(deleteStation);

        assertThat(line.getSections()).hasSize(2);
    }
}