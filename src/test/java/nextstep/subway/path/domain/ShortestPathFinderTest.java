package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Color;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("최단 경로 탐색기")
class ShortestPathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    /**
     * 교대역    --- *2호선* ---      강남역
     * |                                |
     * *3호선*                      *신분당선*
     * |                                |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        강남역 = 지하철_역("강남역");
        양재역 = 지하철_역("양재역");
        교대역 = 지하철_역("교대역");
        남부터미널역 = 지하철_역("남부터미널역");

        신분당선 = 지하철_노선("신분당선", 강남역, 양재역, 10);
        이호선 = 지하철_노선("이호선", 교대역, 강남역, 10);
        삼호선 = 지하철_노선("삼호선", 교대역, 양재역, 5);
        삼호선.addSection(Section.of(교대역, 남부터미널역, Distance.from(3)));
    }

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> ShortestPathFinder.from(Lines.from(Collections.singletonList(신분당선))));
    }

    @Test
    @DisplayName("경로를 조회할 지하철 노선은 필수")
    void instance_emptyList_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> ShortestPathFinder.from(Lines.from(Collections.emptyList())))
            .withMessage("최단 경로를 조회할 노선들이 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("최단 경로")
    void path() {
        // given
        ShortestPathFinder finder = ShortestPathFinder
            .from(Lines.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when
        Path path = finder.path(교대역, 양재역);

        // then
        assertThat(path)
            .isEqualTo(Path.of(
                Stations.from(Arrays.asList(교대역, 남부터미널역, 양재역)),
                Distance.from(5)));
    }

    @Test
    @DisplayName("출발역과 도착역은 달라야 함")
    void path_equalSourceTarget_thrownInvalidDataException() {
        // given
        ShortestPathFinder finder = ShortestPathFinder
            .from(Lines.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when
        ThrowingCallable shortestPathCallable = () -> finder.path(교대역, 교대역);

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(shortestPathCallable)
            .withMessageStartingWith("출발역과 도착역을 동일");
    }

    @Test
    @DisplayName("출발역과 도착역은 지하철 노선 목록에 존재해야 함")
    void path_notExistStation_thrownInvalidDataException() {
        // given
        ShortestPathFinder finder = ShortestPathFinder.from(
            Lines.from(Arrays.asList(신분당선, 이호선, 삼호선)));

        // when
        ThrowingCallable shortestPathCallable = () -> finder.path(교대역, 지하철_역("반포역"));

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(shortestPathCallable)
            .withMessageEndingWith("경로를 조회할 수 없습니다.");
    }

    @Test
    @DisplayName("출발역과 도착역은 연결되어 있어야 함")
    void path_notConnectedStation_thrownInvalidDataException() {
        // given
        ShortestPathFinder finder = ShortestPathFinder.from(Lines.from(Arrays.asList(이호선, 신분당선)));

        // when
        ThrowingCallable shortestPathCallable = () -> finder.path(남부터미널역, 강남역);

        // then
        assertThatExceptionOfType(InvalidDataException.class)
            .isThrownBy(shortestPathCallable)
            .withMessageEndingWith("경로를 조회할 수 없습니다.");
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값으로 최단 경로를 조회할 수 없습니다.")
    @MethodSource
    @DisplayName("경로를 조회할 출발역 또는 도착역은 필수")
    void path_nullStation_thrownInvalidDataException(Station source, Station target) {
        ShortestPathFinder finder = ShortestPathFinder.from(Lines.from(Arrays.asList(신분당선, 삼호선)));

        // when
        ThrowingCallable shortestPathCallable = () -> finder.path(source, target);

        // then
        assertThatIllegalArgumentException()
            .isThrownBy(shortestPathCallable)
            .withMessageEndingWith("null일 수 없습니다.");
    }

    private static Stream<Arguments> path_nullStation_thrownInvalidDataException() {
        return Stream.of(
            Arguments.of(지하철_역("강남역"), null),
            Arguments.of(null, 지하철_역("강남역"))
        );
    }


    private static Station 지하철_역(String name) {
        return Station.from(Name.from(name));
    }

    private static Line 지하철_노선(String name, Station upStation, Station downStation, int distance) {
        return Line.of(Name.from(name), Color.from("red"),
            Sections.from(Section.of(upStation, downStation, Distance.from(distance))));
    }

}
