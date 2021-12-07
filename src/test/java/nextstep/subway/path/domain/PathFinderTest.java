package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PathFinderTest {

    @Autowired
    StationRepository stationRepository;
    @Autowired
    LineRepository lineRepository;

    Line 신분당선;
    Line 이호선;
    Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    /**
     * 교대역       --- *2호선* 10M   ---   강남역
     * |                                      |
     * *3호선*  3M                            *신분당선* 10M
     * |                                      |
     * 남부터미널역  --- *3호선* 5M    ---   양재역
     */
    @BeforeEach
    void setUp() {
        강남역 = 지하철역_등록되어_있음("강남역");
        양재역 = 지하철역_등록되어_있음("양재역");
        교대역 = 지하철역_등록되어_있음("교대역");
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역");

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-800", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-700", 남부터미널역, 양재역, 5);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("경로 파인더 생성")
    @Test
    void of() {
        // given
        List<Line> lines = Arrays.asList(
                신분당선,
                이호선,
                삼호선);
        // when
        PathFinder pathFinder = PathFinder.of(lines);
        // then
        assertThat(pathFinder).isNotNull();
    }

    @DisplayName("경로 찾기")
    @Test
    void find() {
        // given
        Station source = 교대역;
        Station target = 양재역;
        PathFinder pathFinder = PathFinder.of(Arrays.asList(
                신분당선,
                이호선,
                삼호선));
        final List<Station> expectedStations = Arrays.asList(교대역, 남부터미널역, 양재역);
        final int expectedDistance = 8;
        // when
        final Paths paths = pathFinder.findPathBetween(source, target);
        // then
        assertThat(paths.getStations()).containsExactlyElementsOf(expectedStations);
        assertThat(paths.getDistance()).isEqualTo(expectedDistance);
    }


    private Line 지하철_노선_등록되어_있음(String name, String color, Station upStation, Station downStation, int distance) {
        return lineRepository.save(Line.of(name, color, upStation, downStation, distance));
    }

    private Station 지하철역_등록되어_있음(String name) {
        return stationRepository.save(Station.of(name));
    }

    private void 지하철_노선에_지하철역_등록되어_있음(Line line, Station upStation, Station downStation, int distance) {
        line.addSection(Section.of(upStation, downStation, distance));
    }
}