package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    private Sections sections;
    private Line 이호선;
    private Station 사당역;
    private Station 삼성역;
    private Station 강남역;

    @BeforeEach
    void setUp() {
        이호선 = new Line("이호선", "bg-blue");
        사당역 = new Station("사당역");
        강남역 = new Station("강남역");
        sections = new Sections(new Section(이호선, 사당역, 강남역, 10));
        삼성역 = new Station("삼성역");
        sections.addSection(new Section(이호선, 강남역, 삼성역, 3));
    }

    @DisplayName("지하철 역 조회")
    @Test
    void getStations() {
        List<String> stationNames = sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
        assertThat(stationNames).containsExactly("사당역", "강남역", "삼성역");
    }

}
