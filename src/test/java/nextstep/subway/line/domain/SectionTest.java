package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionTest {
    private Section section;

    @BeforeEach
    void setUp() {
        this.section = new Section(new Line("3호선", "orange darken-1"), new Station("교대역"), new Station("양재역"), 10);
    }

    @Test
    void updateUpStation_수행시_새_거리는_기존_거리보다_작아야_한다() {
        //when
        assertThatThrownBy(() -> section.updateUpStation(new Station("고속터미널역"), 10)).isExactlyInstanceOf(
                RuntimeException.class);
        section.updateUpStation(new Station("남부터미널역"), 9);

        //then
        assertThat(section.getDistance()).isEqualTo(1);
        assertThat(section.getUpStation().getName()).isEqualTo("남부터미널역");
    }

    @Test
    void updateDownStation_수행시_새_거리는_기존_거리보다_작아야_한다() {
        //when
        assertThatThrownBy(() -> section.updateDownStation(new Station("고속터미널역"), 10)).isExactlyInstanceOf(
                RuntimeException.class);
        section.updateDownStation(new Station("남부터미널역"), 9);

        //then
        assertThat(section.getDistance()).isEqualTo(1);
        assertThat(section.getDownStation().getName()).isEqualTo("남부터미널역");
    }
}
