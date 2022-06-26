package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineSurchargePolicyTest {
    private List<Line> lines;
    private LineSurchargePolicy lineSurchargePolicy;

    @BeforeEach
    void setUp() {
        Station upStation = new Station("강남역");
        Station downStation = new Station("양재역");

        Station upStation2 = new Station("양재역");
        Station downStation2 = new Station("남부터미널역");

        Line line = new Line("신분당선", "bg-red-600", 500, upStation, downStation, 10);
        Line line2 = new Line("삼호선", "bg-orange-600", 750, upStation2, downStation2, 10);

        lines = Arrays.asList(line, line2);

        lineSurchargePolicy = new LineSurchargePolicy(() -> Fare.BASIC_FARE, lines);
    }

    @Test
    void 추가_요금이_있는_노선을_이용할_경우_가장_높은_추가_요금만_적용한다() {
        // when
        Fare result = lineSurchargePolicy.fare();

        // then
        assertThat(result.value()).isEqualTo(2000);
    }
}
