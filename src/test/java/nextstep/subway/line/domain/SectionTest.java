package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionTest {
    private static Line 신분당선;
    private static Station 강남;
    private static Station 양재시민의숲;
    private static Station 양재;

    @BeforeAll
    static void setUp() {
        신분당선 = new Line("신분당선", "green");
        강남 = new Station("강남");
        양재시민의숲 = new Station("양재시민의숲");
        양재 = new Station("양재");
    }

    @DisplayName("구간 결합")
    @Test
    void combine() {
        // given
        Section section1 = new Section(신분당선, 양재시민의숲, 양재, 5);
        Section section2 = new Section(신분당선, 강남, 양재시민의숲, 5);

        // when
        Section newSection = Section.combine(section1, section2);

        // then
        assertThat(newSection.getUpStation()).isEqualTo(강남);
        assertThat(newSection.getDownStation()).isEqualTo(양재);
        assertThat(newSection.getDistance()).isEqualTo(10);
    }

    @DisplayName("앞 구간 축소 업데이트")
    @Test
    void updateUpStation() {
        // given
        Section section = new Section(신분당선, 강남, 양재, 10);

        // when
        section.updateUpStation(양재시민의숲, 5);

        // then
        assertThat(section.getUpStation()).isEqualTo(양재시민의숲);
        assertThat(section.getDownStation()).isEqualTo(양재);
        assertThat(section.getDistance()).isEqualTo(5);
    }

    @DisplayName("뒷 구간 축소 업데이트")
    @Test
    void updateDownStation() {
        // given
        Section section = new Section(신분당선, 강남, 양재, 10);

        // when
        section.updateDownStation(양재시민의숲, 5);

        // then
        assertThat(section.getUpStation()).isEqualTo(강남);
        assertThat(section.getDownStation()).isEqualTo(양재시민의숲);
        assertThat(section.getDistance()).isEqualTo(5);
    }
}
