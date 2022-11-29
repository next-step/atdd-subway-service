package nextstep.subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

class SectionTest {
    private final Line 신분당선 = Line.of("신분당선", "red");
    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 판교역 = new Station("판교역");
    private final Distance TEN = Distance.from(10);
    private final Distance SIX = Distance.from(6);
    private final Distance FOUR = Distance.from(4);
    private final Distance FIFTY = Distance.from(15);

    @Test
    void 생성() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        assertAll(
                () -> assertThat(section.getLine().getName()).isEqualTo("신분당선"),
                () -> assertThat(section.getLine().getColor()).isEqualTo("red"),
                () -> assertThat(section.getUpStation()).isEqualTo(강남역),
                () -> assertThat(section.getDownStation()).isEqualTo(광교역),
                () -> assertThat(section.getDistance()).isEqualTo(TEN)
        );
    }

    @Test
    void 상행역_수정() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        section.updateUpStation(판교역, FOUR);
        assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(판교역),
                () -> assertThat(section.getDistance()).isEqualTo(SIX)
        );
    }

    @Test
    void 하행역_수정() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        section.updateDownStation(판교역, FOUR);
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(판교역),
                () -> assertThat(section.getDistance()).isEqualTo(SIX)
        );
    }

    @Test
    void 역과_역_사이_거리_초과_입력() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        assertThatThrownBy(
                () -> section.updateUpStation(판교역, FIFTY))
        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 상행역과_같은지_검사() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        assertThat(section.isSameUpStation(강남역)).isTrue();
        assertThat(section.isSameUpStation(광교역)).isFalse();
    }

    @Test
    void 하행역과_같은지_검사() {
        Section section = Section.of(신분당선, 강남역, 광교역, TEN);
        assertThat(section.isSameDownStation(광교역)).isTrue();
        assertThat(section.isSameDownStation(강남역)).isFalse();
    }
}
