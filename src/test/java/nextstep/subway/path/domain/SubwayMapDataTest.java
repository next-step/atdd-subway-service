package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayMapDataTest {
    private Station 교대역;
    private Station 강남역;
    private Line 이호선;
    private List<Line> 노선도;

    @BeforeEach
    void setUp() {
        교대역 = initStation("교대역", 1L);
        강남역 = initStation("강남역", 2L);
        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);

        노선도 = Arrays.asList(이호선);
    }

    @DisplayName("지하철 노선 데이터를 WeightedMultigraph 에 셋팅한다.")
    @Test
    void initData() {
        SubwayMapData subwayMapData = new SubwayMapData(노선도, SectionEdge.class);

        assertThat(subwayMapData.initData().vertexSet()).isNotEmpty();
        assertThat(subwayMapData.initData().edgeSet()).isNotEmpty();
    }

    @DisplayName("노선도 값이 들어오지 않을 경우 예외가 발생한다")
    @Test
    void validateData() {
        assertThatThrownBy(() -> new SubwayMapData(new ArrayList<>(), SectionEdge.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경로 조회에 필요한 노선도 값이 조회되지 않습니다.");
    }

    private Station initStation(String name, Long id) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}
