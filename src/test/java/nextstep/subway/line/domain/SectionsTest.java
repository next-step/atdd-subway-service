package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

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
    void 상행으로_비교하여_구간을_구할_수_있다() {
        Sections sections = new Sections();
        Section firstSection = new Section(신분당선, 강남역, 양재역, 10);
        Section secondSection = new Section(신분당선, 양재역, 신논현역, 5);
        sections.add(firstSection);
        sections.add(secondSection);

        assertThat(sections.getNextSectionByEqualUpStation(강남역)).isEqualTo(Optional.of(firstSection));
        assertThat(sections.getNextSectionByEqualUpStation(양재역)).isEqualTo(Optional.of(secondSection));
        assertThat(sections.getNextSectionByEqualUpStation(신논현역)).isEqualTo(Optional.empty());
    }

    @Test
    void 하행으로_비교하여_구간을_구할_수_있다() {
        Sections sections = new Sections();
        Section firstSection = new Section(신분당선, 강남역, 양재역, 10);
        Section secondSection = new Section(신분당선, 양재역, 신논현역, 5);
        sections.add(firstSection);
        sections.add(secondSection);

        assertThat(sections.getNextSectionByEqualDownStation(강남역)).isEqualTo(Optional.empty());
        assertThat(sections.getNextSectionByEqualDownStation(양재역)).isEqualTo(Optional.of(firstSection));
        assertThat(sections.getNextSectionByEqualDownStation(신논현역)).isEqualTo(Optional.of(secondSection));
    }

}