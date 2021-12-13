package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("경로 관련 기능")
class PathTest {

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
    void 경로_생성() {
        // given - when
        Path actual = Path.of(Arrays.asList(강남역, 양재역, 남부터미널역), 12);

        // then
        Assertions.assertThat(actual).isNotNull();
    }
}
