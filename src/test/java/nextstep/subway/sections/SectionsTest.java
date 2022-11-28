package nextstep.subway.sections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class SectionsTest {
    private final Line 신분당선 = Line.of("신분당선", "red");
    private final Station 강남역 = new Station("강남역");
    private final Station 광교역 = new Station("광교역");
    private final Station 판교역 = new Station("판교역");
    private final Station 정자역 = new Station("정자역");

    @Test
    void 노선_조회() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);
        sections.addSection(신분당선, 강남역, 판교역, 5);

        assertAll(
                () -> assertThat(sections.getStations().get(0)).isEqualTo(강남역),
                () -> assertThat(sections.getStations().get(1)).isEqualTo(판교역),
                () -> assertThat(sections.getStations().get(2)).isEqualTo(광교역)
        );
    }

    @Test
    void 노선_추가() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);

        assertThat(sections.getSize()).isEqualTo(1);
    }

    @Test
    void 노선_추가_2() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);
        sections.addSection(신분당선, 강남역, 판교역, 5);

        assertAll(
                () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(판교역),
                () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(광교역),
                () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5),
                () -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(강남역),
                () -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(판교역),
                () -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(5)
        );
    }

    @Test
    void 노선_제거() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);
        sections.addSection(신분당선, 강남역, 판교역, 2);
        sections.addSection(신분당선, 판교역, 정자역, 2);

        sections.removeStation(신분당선, 판교역);

        assertThat(sections.getSize()).isEqualTo(2);
    }

    @Test
    void 노선_제거_2() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);
        sections.addSection(신분당선, 강남역, 판교역, 5);

        sections.removeStation(신분당선, 판교역);

        assertAll(
                () -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
                () -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(광교역),
                () -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10)
        );
    }

    @Test
    void 이미_등록된_구간을_등록하려_하는_경우() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);

        assertThatThrownBy(
                () -> sections.addSection(신분당선, 강남역, 광교역, 10))
        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 상행역_하행역_둘_다_존재하지_않는_경우() {
        Sections sections = new Sections();
        sections.addSection(신분당선, 강남역, 광교역, 10);

        assertThatThrownBy(
                () -> sections.addSection(신분당선, 판교역, 정자역, 5))
        .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 노선에_역이_없는데_삭제하려_하는_경우() {
        Sections sections = new Sections();

        assertThatThrownBy(
                () -> sections.removeStation(신분당선, 판교역)
        ).isInstanceOf(RuntimeException.class);
    }
}
