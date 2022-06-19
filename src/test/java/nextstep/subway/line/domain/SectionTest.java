package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    private final static Line 신분당선 = new Line("신분당선", "red");
    private final static Station 신논현역 = new Station("신논현");
    private final static Station 강남역 = new Station("강남역");
    private final static Station 양재역 = new Station("양재역");

    @Test
    void 구간을_재배치한다() {
        // given
        Section section1 = new Section(신분당선, 신논현역, 강남역, 5);
        Section section2 = new Section(신분당선, 강남역, 양재역, 3);

        // when
        Section expected = new Section(신분당선, 신논현역, 양재역, 8);
        Section actual = Section.merge(신분당선, section1, section2);

        // then
        assertThat(actual).isEqualTo(expected);
    }

}