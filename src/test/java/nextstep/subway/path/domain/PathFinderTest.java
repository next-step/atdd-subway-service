package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("경로 탐색 테스트")
class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 용산역;
    private Station 신림역;
    private Station 봉천역;
    private Sections 전체_구간;


    @BeforeEach
    public void setup() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        용산역 = new Station("용산역");
        신림역 = new Station("신림역");
        봉천역 = new Station("봉천역");

        Line 신분당선 = new Line("신분당선", "bg-red-600");
        Line 이호선 = new Line("신분당선", "bg-red-600");
        Line 삼호선 = new Line("신분당선", "bg-red-600");

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(신분당선, 강남역, 양재역, 10));
        sections.add(new Section(이호선, 교대역, 강남역, 10));
        sections.add(new Section(이호선, 신림역, 봉천역, 10));
        sections.add(new Section(삼호선, 교대역, 남부터미널역, 2));
        sections.add(new Section(삼호선, 남부터미널역, 양재역, 3));

        전체_구간 = Sections.of(sections);
    }

    @DisplayName("최단 경로 탐색")
    @Test
    void 최단_경로_탐색() {
        // given
        PathFinder pathFinder = PathFinder.of(전체_구간);

        // when
        Path shortestPath = pathFinder.findShortestPath(교대역, 양재역);

        // then
        assertAll(
                () -> assertThat(shortestPath.getPathSize())
                        .isEqualTo(3),
                () -> 최단_경로_확인(shortestPath, Arrays.asList(교대역, 남부터미널역, 양재역))
        );
    }

    @DisplayName("실패 테스트")
    @Nested
    class FailTest {

        @DisplayName("빈 구간으로 생성")
        @Test
        void 빈_구간으로_생성() {
            // given
            Sections sections = Sections.of(Collections.emptyList());

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> PathFinder.of(sections);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }
        @DisplayName("출발역과_도착역이_같음")
        @Test
        void 출발지와_목적지가_같음() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 강남역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("출발역이 null")
        @Test
        void 출발지가_null() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(null, 강남역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("도착역이_null")
        @Test
        void 목적지가_null() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(남부터미널역, null);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("출발역이 전체 구간에 포함되지 않음")
        @Test
        void 출발역이_전체_구간에_포함되지_않음() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(용산역, 강남역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("도착역이 전체 구간에 포함되지 않음")
        @Test
        void 도착역이_전체_구간에_포함되지_않음() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 용산역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 출발역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_출발역_기준() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(신림역, 강남역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("출발역과 도착역이 연결되어 있지 않음 - 도착역 기준")
        @Test
        void 출발역과_도착역이_연결되어_있지_않음_도착역_기준() {
            // given
            PathFinder pathFinder = PathFinder.of(전체_구간);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 봉천역);

            // then
            assertThatThrownBy(throwingCallable)
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    public static void 최단_경로_확인(Path shortestPath, List<Station> expectedStations) {
        assertThat(shortestPath.getStations())
                .containsExactlyElementsOf(expectedStations);
    }
}