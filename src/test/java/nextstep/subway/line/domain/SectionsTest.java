package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
        Section firstSection = new Section(신분당선, 강남역, 양재역, new Distance(10));
        Section secondSection = new Section(신분당선, 양재역, 신논현역, new Distance(5));
        sections.add(firstSection);
        sections.add(secondSection);

        assertThat(sections.getNextSectionByEqualUpStation(강남역)).isEqualTo(Optional.of(firstSection));
        assertThat(sections.getNextSectionByEqualUpStation(양재역)).isEqualTo(Optional.of(secondSection));
        assertThat(sections.getNextSectionByEqualUpStation(신논현역)).isEmpty();
    }

    @Test
    void 하행으로_비교하여_구간을_구할_수_있다() {
        Sections sections = new Sections();
        Section firstSection = new Section(신분당선, 강남역, 양재역, new Distance(10));
        Section secondSection = new Section(신분당선, 양재역, 신논현역, new Distance(5));
        sections.add(firstSection);
        sections.add(secondSection);

        assertThat(sections.getNextSectionByEqualDownStation(강남역)).isEmpty();
        assertThat(sections.getNextSectionByEqualDownStation(양재역)).isEqualTo(Optional.of(firstSection));
        assertThat(sections.getNextSectionByEqualDownStation(신논현역)).isEqualTo(Optional.of(secondSection));
    }

    @Test
    void add_비어있는_구간리스트_구간추가() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));

        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void add_하행_구간추가() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 양재역, 신논현역, new Distance(5)));

        assertThat(sections.size()).isEqualTo(2);
    }

    @Test
    void add_상행_구간추가() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 신논현역, 강남역, new Distance(5)));

        assertThat(sections.size()).isEqualTo(2);
    }

    @Test
    void add_구간사이_구간추가() {
        Sections sections = new Sections();
        Station 양재시민의숲 = new Station("양재시민의숲");
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 양재역, 신논현역, new Distance(5)));
        sections.add(new Section(신분당선, 양재역, 양재시민의숲, new Distance(3)));

        assertThat(sections.size()).isEqualTo(3);
    }

    @Test
    void add_이미_등록된_구간추가_실패() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.add(new Section(신분당선, 강남역, 양재역, new Distance(5))));
    }

    @Test
    void add_상행_하행_매칭되는_구간이_아예_없는_구간추가_실패() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));

        Station 양재시민의숲 = new Station("양재시민의숲");

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.add(new Section(신분당선, 신논현역, 양재시민의숲, new Distance(5))));
    }

    @Test
    void removeLineStation_상행역_삭제() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 신논현역, 강남역, new Distance(5)));

        sections.remove(신분당선, 강남역);

        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_하행역_삭제() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 신논현역, 강남역, new Distance(5)));

        sections.remove(신분당선, 신논현역);

        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_구간사이_역_삭제() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));
        sections.add(new Section(신분당선, 신논현역, 강남역, new Distance(5)));

        sections.remove(신분당선, 양재역);

        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void removeLineStation_구간이_하나_이하인_경우에는_삭제_불가() {
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 양재역, new Distance(10)));

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> sections.remove(신분당선, 양재역));
    }

}