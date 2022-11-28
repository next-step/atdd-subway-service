package nextstep.subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class SectionTest {
    private Line 신분당선 = Line.of("신분당선", "red");
    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");
    private Station 판교역 = new Station("판교역");

    @Test
    void 생성() {
        Section section = new Section(신분당선, 강남역, 광교역, 10);
        assertAll(
                () -> assertThat(section.getLine().getName()).isEqualTo("신분당선"),
                () -> assertThat(section.getLine().getColor()).isEqualTo("red"),
                () -> assertThat(section.getUpStation()).isEqualTo(강남역),
                () -> assertThat(section.getDownStation()).isEqualTo(광교역),
                () -> assertThat(section.getDistance()).isEqualTo(10)
        );
    }

    @Test
    void 상행역_수정() {
        Section section = new Section(신분당선, 강남역, 광교역, 10);
        section.updateUpStation(판교역, 4);
        assertAll(
                () -> assertThat(section.getUpStation()).isEqualTo(판교역),
                () -> assertThat(section.getDistance()).isEqualTo(6)
        );
    }

    @Test
    void 하행역_수정() {
        Section section = new Section(신분당선, 강남역, 광교역, 10);
        section.updateDownStation(판교역, 4);
        assertAll(
                () -> assertThat(section.getDownStation()).isEqualTo(판교역),
                () -> assertThat(section.getDistance()).isEqualTo(6)
        );
    }

    @Test
    void 역과_역_사이_거리_초과_입력() {
        Section section = new Section(신분당선, 강남역, 광교역, 10);
        assertThatThrownBy(
                () -> section.updateUpStation(판교역, 15))
        .isInstanceOf(RuntimeException.class);
    }
}
