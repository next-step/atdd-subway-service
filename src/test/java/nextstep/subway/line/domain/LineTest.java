package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    private Station 강남역;
    private Station 양재역;
    private Station 신논현역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        신논현역 = new Station("신논현역");
    }

    @Test
    void 정렬된_역_리스트를_구할_수_있다() {
        Line line = new Line("라인", "red", 강남역, 양재역, 10);
        line.addSection(new Section(line, 양재역, 신논현역, 5));

        assertThat(line.getOrderedStations()).containsExactly(강남역, 양재역, 신논현역);
    }
}
