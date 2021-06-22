package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

class SubwayNavigationTest {

    /**
     * 교대역   --- *2호선*(10)  ---     강남역
     * |                                 |
     * *3호선*(7)                     *신분당선* (4)
     * |                                |
     * 남부터미널역 --- *3호선*(10) --- 양재
     *
     * 춘천역 -----*춘천강원선*(20)------- 강원역
     */

    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Station 춘천역;
    private Station 강원역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private Line 춘천강원선;
    private List<Line> 노선도;

    @BeforeEach
    void setUp() {
        교대역 = initStation("교대역", 1L);
        남부터미널역 = initStation("남부터미널역", 2L);
        양재역 = initStation("양재역", 3L);
        강남역 = initStation("강남역", 4L);
        춘천역 = initStation("춘천역", 5L);
        강원역 = initStation("강원역", 6L);

        이호선 = new Line("2호선", "green", 교대역, 강남역, 10);
        삼호선 = new Line("3호선", "orange", 교대역, 양재역, 17);
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4);
        춘천강원선 = new Line("춘천강원선", "sky", 춘천역, 강원역, 20);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 7));
        노선도 = Arrays.asList(이호선, 삼호선, 신분당선, 춘천강원선);
    }


    @DisplayName("경로 내의 지하철역들을 조회한다.")
    @Test
    void getPaths() {

    }

    @Test
    void getDistance() {
    }

    private Station initStation(String name, Long id) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }
}