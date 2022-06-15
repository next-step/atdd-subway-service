package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    private Station 강남역;
    private Station 양재역;
    private Station 신논현역;
    private Line 신분당선;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        신논현역 = new Station("신논현역");
        신분당선 = new Line("신분당선", "red");
    }

    @Test
    void 인자로_받은_구간에서_하행선_역과_거리를_합칠_수_있다() {
        Section section = new Section(신분당선, 강남역, 양재역, new Distance(10));
        Section newSection = section.combine(new Section(신분당선, 양재역, 신논현역, new Distance(5)));

        assertThat(newSection.getDistance()).isEqualTo(new Distance(15));
        assertThat(newSection.getUpStation()).isEqualTo(강남역);
        assertThat(newSection.getDownStation()).isEqualTo(신논현역);
    }

}