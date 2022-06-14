package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class LineTest {

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
        Line line = new Line("라인", "red", 강남역, 양재역, new Distance(10));
        line.addSection(new Section(line, 양재역, 신논현역, new Distance(5)));

        assertThat(line.getOrderedStations()).containsExactly(강남역, 양재역, 신논현역);
    }

    @Test
    void removeLineStation_상행역_삭제() {
        Line line = new Line("라인", "red", 강남역, 양재역, new Distance(10));
        line.addSection(new Section(line, 양재역, 신논현역, new Distance(5)));

        line.removeLineStation(강남역);

        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_하행역_삭제() {
        Line line = new Line("라인", "red", 강남역, 양재역, new Distance(10));
        line.addSection(new Section(line, 양재역, 신논현역, new Distance(5)));

        line.removeLineStation(신논현역);

        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_구간사이_역_삭제() {
        Line line = new Line("라인", "red", 강남역, 양재역, new Distance(10));
        line.addSection(new Section(line, 양재역, 신논현역, new Distance(5)));

        line.removeLineStation(양재역);

        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_구간이_하나_이하인_경우에는_삭제_불가() {
        Line line = new Line("라인", "red", 강남역, 양재역, new Distance(10));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.removeLineStation(양재역));
    }
}
