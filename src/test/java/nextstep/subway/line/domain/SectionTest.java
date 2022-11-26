package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {

    @DisplayName("지하철 노선, 상행역, 하행역이 같은 두 지하철 구간은 동등하다.")
    @Test
    void equals() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("신분당선", "red");
        Station 강남역1 = new Station("강남역");
        Station 강남역2 = new Station("강남역");
        Station 광교역1 = new Station("광교역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(line1, 강남역1, 광교역1, 10);
        Section section2 = new Section(line2, 강남역2, 광교역2, 10);

        Assertions.assertThat(section1).isEqualTo(section2);
    }

    @DisplayName("지하철 노선이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals1() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("분당선", "yellow");
        Station 강남역1 = new Station("강남역");
        Station 강남역2 = new Station("강남역");
        Station 광교역1 = new Station("광교역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(line1, 강남역1, 광교역1, 10);
        Section section2 = new Section(line2, 강남역2, 광교역2, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }

    @DisplayName("상행역이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals2() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("신분당선", "red");
        Station 강남역1 = new Station("강남역");
        Station 신사역 = new Station("신사역");
        Station 광교역1 = new Station("광교역");
        Station 광교역2 = new Station("광교역");

        Section section1 = new Section(line1, 강남역1, 광교역1, 10);
        Section section2 = new Section(line2, 신사역, 광교역2, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }

    @DisplayName("하행역이 다른 두 지하철 구간은 동등하지 않다.")
    @Test
    void notEquals3() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("신분당선", "red");
        Station 강남역1 = new Station("강남역");
        Station 강남역2 = new Station("강남역");
        Station 광교역1 = new Station("광교역");
        Station 양재역 = new Station("양재역");

        Section section1 = new Section(line1, 강남역1, 광교역1, 10);
        Section section2 = new Section(line2, 강남역2, 양재역, 10);

        Assertions.assertThat(section1).isNotEqualTo(section2);
    }
}
