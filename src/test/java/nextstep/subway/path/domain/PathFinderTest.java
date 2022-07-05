package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import nextstep.subway.exception.domain.SubwayException;
import nextstep.subway.exception.domain.SubwayExceptionMessage;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class PathFinderTest {

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 500 , 강남역, 양재역, 10);
        이호선 = new Line("이호선", "bg-red-600", 900 ,교대역, 강남역, 10);
        삼호선 = new Line("삼호선", "bg-red-600",  0, 교대역, 양재역, 5);

        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 3));

        pathFinder = new PathFinder(new Lines(Arrays.asList(신분당선 ,이호선, 삼호선)));
    }

    @Test
    @DisplayName("최단경로 찾기")
    void find() {
        // when
        final Sections sections = pathFinder.find(강남역, 남부터미널역);

        // then
        final Section 강남역_양재역_구간 = new Section(null, 강남역, 양재역, 10);
        final Section 양재역_남부터미널역_구간 = new Section(null, 양재역, 남부터미널역, 2);
        final Sections expected = new Sections(Arrays.asList(강남역_양재역_구간, 양재역_남부터미널역_구간));
        assertThat(sections).isEqualTo(expected);
    }

    @Test
    @DisplayName("출발역과 도착역이 같을때")
    void pathFind_exception_same_station() {
        // when & then
        assertThatThrownBy(() -> pathFinder.find(강남역, 강남역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining(SubwayExceptionMessage.EQUALS_START_AND_END_STATION.getMessage());
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되지 않을때")
    void pathFind_exception_not_linked() {
        // given
        final Station 당고개역 = new Station("당고개역");
        final Station 남태령역 = new Station("남태령역");
        final Line 사호선 = new Line("사호선", "bg-red-600", 0, 당고개역, 남태령역, 10);

        final PathFinder exceptionPathFinder = new PathFinder(new Lines(Arrays.asList(신분당선, 이호선, 삼호선, 사호선)));

        // when & then
        assertThatThrownBy(() -> exceptionPathFinder.find(강남역, 남태령역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining(SubwayExceptionMessage.NOT_LINKED_STATION.getMessage());
    }

    @Test
    @DisplayName("출발역 또는 도착역이 존재하지 않을때")
    void pathFind_exception_not_found() {
        // given
        final Station 남태령역 = new Station("남태령역");

        // when & then
        assertThatThrownBy(() -> pathFinder.find(강남역, 남태령역))
                .isInstanceOf(SubwayException.class)
                .hasMessageContaining(SubwayExceptionMessage.NOT_FOUND_STATION.getMessage());
    }
}
