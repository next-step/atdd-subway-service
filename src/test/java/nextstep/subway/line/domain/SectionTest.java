package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    private Line line = new Line("3호선", "orange darken-1");
    private Station 교대역 = new Station("교대역");
    private Station 양재역 = new Station("양재역");
    private Station 고속터미널역 = new Station("고속터미널역");

    @Test
    void Section_생성시_거리는_1_이상이어야_한다() {
        assertThatThrownBy(() -> new Section(line, 교대역, 양재역, 0)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateUpStation_수행시_새_거리는_1_이상이고_기존_거리보다_작아야_한다() {
        //given
        Section section = new Section(line, 교대역, 양재역, 10);

        //when
        assertThatThrownBy(() -> section.updateUpStation(고속터미널역, 10)).isExactlyInstanceOf(
                IllegalArgumentException.class);
        assertThatThrownBy(() -> section.updateUpStation(고속터미널역, -1)).isExactlyInstanceOf(
                IllegalArgumentException.class);
        section.updateUpStation(고속터미널역, 9);

        //then
        assertThat(section.getDistance()).isEqualTo(1);
        assertThat(section.getUpStation().getName()).isEqualTo("고속터미널역");
        assertThat(section.getDownStation().getName()).isEqualTo("양재역");
    }

    @Test
    void updateDownStation_수행시_새_거리는_1_이상이고_기존_거리보다_작아야_한다() {
        //given
        Section section = new Section(line, 교대역, 양재역, 10);

        //when
        assertThatThrownBy(() -> section.updateDownStation(고속터미널역, 10)).isExactlyInstanceOf(
                IllegalArgumentException.class);
        assertThatThrownBy(() -> section.updateDownStation(고속터미널역, -1)).isExactlyInstanceOf(
                IllegalArgumentException.class);
        section.updateDownStation(고속터미널역, 9);

        //then
        assertThat(section.getDistance()).isEqualTo(1);
        assertThat(section.getUpStation().getName()).isEqualTo("교대역");
        assertThat(section.getDownStation().getName()).isEqualTo("고속터미널역");
    }
}
