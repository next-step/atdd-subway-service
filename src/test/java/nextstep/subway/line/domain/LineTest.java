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
        Line line = new Line("라인", "red", 강남역, 양재역, 10);
        line.addSection(new Section(line, 양재역, 신논현역, 5));

        assertThat(line.getOrderedStations()).containsExactly(강남역, 양재역, 신논현역);
    }

    @Test
    void addLineStation_비어있는_구간리스트_구간추가() {
        Line line = new Line("라인", "red");
        line.addLineStation(강남역, 양재역, 10);

        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @Test
    void addLineStation_하행_구간추가() {
        Line line = new Line("라인", "red", 강남역, 양재역, 10);
        line.addLineStation(양재역, 신논현역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @Test
    void addLineStation_상행_구간추가() {
        Line line = new Line("라인", "red", 강남역, 양재역, 10);
        line.addLineStation(신논현역, 강남역, 5);

        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @Test
    void addLineStation_구간사이_구간추가() {
        Station 양재시민의숲 = new Station("양재시민의숲");
        Line line = new Line("라인", "red", 강남역, 양재역, 10);

        line.addLineStation(양재역, 신논현역, 5);
        line.addLineStation(양재역, 양재시민의숲, 3);

        assertThat(line.getSections().size()).isEqualTo(3);
    }

    @Test
    void addLineStation_이미_등록된_구간추가_실패() {
        Line line = new Line("라인", "red", 강남역, 양재역, 10);

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addLineStation(강남역, 양재역, 5));
    }

    @Test
    void addLineStation_상행_하행_매칭되는_구간이_아예_없는_구간추가_실패() {
        Line line = new Line("라인", "red", 강남역, 양재역, 10);

        Station 양재시민의숲 = new Station("양재시민의숲");

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> line.addLineStation(신논현역, 양재시민의숲, 5));
    }
}
