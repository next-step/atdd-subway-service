package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Line 일호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 천안역;
    private Station 온양온천역;
    private PathFinder pathFinder;
    private List<Section> sections;

    /**
     * 교대역    --- *2호선*10 ---   강남역
     * |                             |
     * *3호선*3                     *신분당선*10
     * |                             |
     * 남부터미널역  --- *3호선*2 ---   양재
     *
     * 천안역 ----- *1호선*7 ------ 온양온천역
     */

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        천안역 = new Station("천안역");
        온양온천역 = new Station("온양온천역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
        일호선 = new Line("일호선", "bg-red-600", 천안역, 온양온천역, 7);
        이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600", 교대역, 남부터미널역, 3);

        삼호선.addSection(new Section(삼호선,남부터미널역, 양재역, 2));

        pathFinder = new PathFinder();
        sections = getSections(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @Test
    @DisplayName("지하철 경로를 조회한다.")
    void manageFindPath() {

        // when
        Path path = pathFinder.findPath(sections, 강남역, 남부터미널역);

        // then
        assertThat(path.getStations()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat(path.getDistance()).isEqualTo(12);

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 출발역과 도착역이 동일한 경우")
    void manageFindSameStation() {

        // then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findPath(sections, 강남역, 강남역));

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 출발역과 도착역이 연결되어 있지 않아 경로를 찾을 수 없음.")
    void manageFindNoPath() {

        // then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findPath(sections, 천안역, 강남역));

    }

    @Test
    @DisplayName("지하철 경로를 실패한다. - 존재하지 않은 지하철역이 있음.")
    void manageFindNoStation() {
        // when
        Station 미존재역 = new Station("미존재역");

        // then
        assertThrows(IllegalArgumentException.class, () -> pathFinder.findPath(sections, 미존재역, 강남역));

    }

    private List<Section> getSections(List<Line> lineList) {
        return lineList.stream()
                .map(Line::getSections)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
