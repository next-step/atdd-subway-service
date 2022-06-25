package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class LineFarePolicyTest {

    private final LineFarePolicy policy = new LineFarePolicy();
    private Line 신분당선;
    private Line 일호선;
    private Station 강남역;
    private Station 양재역;
    private Station 수원역;
    private Station 서울역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        수원역 = new Station("수원역");
        서울역 = new Station("서울역");

        신분당선 = new Line("신분당선", "아무색", 강남역, 양재역, 10, new Fare(900));
        일호선 = new Line("일호선", "파랑색", 서울역, 수원역, 50, new Fare(100));
    }

    @Test
    void 노선의_추가요금() {
        assertThat(policy.calculate(Arrays.asList(일호선, 신분당선))).isEqualTo(new Fare(900));
    }
}


