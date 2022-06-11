package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {
    private Station 교대역;
    private Station 강남역;
    private Station 삼성역;
    private Station 잠실역;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        삼성역 = new Station("삼성역");
        잠실역 = new Station("잠실역");

        이호선 = new Line("이호선", "green");
    }

    @DisplayName("지하철 구간의 역 제거 테스트")
    @Test
    void removeSectionByStation() {
        이호선.addSection(교대역, 강남역, 5);
        이호선.addSection(강남역, 삼성역, 5);
        이호선.addSection(삼성역, 잠실역, 5);
        이호선.removeSectionByStation(삼성역);

        지하철역_순서_정렬됨(이호선.getSections(), Arrays.asList(교대역, 강남역, 잠실역));
    }

    @DisplayName("지하철 구간의 역 제거 예외 테스트 - 구간이 하나 뿐인 경우")
    @Test
    void removeSectionByStation_exception() {
        이호선.addSection(강남역, 삼성역, 5);

        assertThatThrownBy(() -> 이호선.removeSectionByStation(삼성역)).isInstanceOf(RuntimeException.class);
    }

    public static void 지하철역_순서_정렬됨(Sections sections, List<Station> expectedStations) {
        List<String> stationNames = sections.findStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        List<String> expectedStationNames = expectedStations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertThat(stationNames).containsExactlyElementsOf(expectedStationNames);
    }
}
