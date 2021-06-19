package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {

    private Line line;
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private Station 군자역;

    @BeforeEach
    void setUp() {
        강남역 = Station.of("강남역");
        역삼역 = Station.of("역삼역");
        삼성역 = Station.of("삼성역");
        군자역 = Station.of("군자역");
        line = new Line("1호선", "파란색");
    }

    @DisplayName("모든 구간들에서 가장 최상행 역을 찾는다.")
    @Test
    void findMostTopStationTest() {
        Section section1 = new Section(line, 강남역, 역삼역, 10);
        Section section2 = new Section(line, 역삼역, 군자역, 10);
        Section section3 = new Section(line, 삼성역, 강남역, 5);
        line.addSection(section1);
        line.addSection(section2);
        line.addSection(section3);

        Sections sections = line.getSections();

        assertThat(sections.findTopMostStation()).isEqualTo(삼성역);
    }
}
