package nextstep.subway.path.domain;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("경로 조회 관련 기능")
class PathFinderTest {

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    @BeforeEach
    void setUp() {

        강남역 = Station.from("강남역");
        양재역 = Station.from("양재역");
        교대역 = Station.from("교대역");
        남부터미널역 = Station.from("남부터미널역");

        신분당선 = Line.of("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = Line.of("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = Line.of("삼호선", "bg-red-600", 교대역, 양재역, 5);
        삼호선.addSection(교대역, 남부터미널역, 3);
    }

    @Test
    void 최단_경로_조회() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));
        PathFinder pathFinder = PathFinder.from(lines);

        // when
        Path actual = pathFinder.findShortestPath(강남역, 남부터미널역);

        // then
        Assertions.assertThat(actual.getStations()).hasSize(3);
        Assertions.assertThat(actual.getDistance()).isEqualTo(12);
    }

    @Test
    void 최단_경로_조회_출발역과_도착역이_같은_경우_조회할_수_없다() {
        // given
        Lines lines = Lines.from(Arrays.asList(신분당선, 이호선, 삼호선));
        PathFinder pathFinder = PathFinder.from(lines);

        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> pathFinder.findShortestPath(강남역, 강남역);

        // then
        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(throwingCallable);
    }
}
