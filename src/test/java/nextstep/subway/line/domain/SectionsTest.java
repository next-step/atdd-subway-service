package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SectionsTest {

    private static final Line 신분당선 = new Line("신분당선", "bg-red-600");
    private static final Line 분당선 = new Line("분당선", "bg-green-600");
    private static final Station 강남역 = new Station("강남역");
    private static final Station 서초역 = new Station("서초역");
    private static final Station 반포역 = new Station("반포역");
    private static final Station 삼성역 = new Station("삼성역");
    private static final Station 역삼역 = new Station("역삼역");
    private static final Station 안양역 = new Station("안양역");
    private static final Station 범계역 = new Station("범계역");

    private Sections existSections = new Sections();

    @BeforeEach
    void setup() {
        existSections.add(new Section(분당선, 강남역, 서초역, 2));
        existSections.add(new Section(분당선, 서초역, 반포역, 3));
        existSections.add(new Section(분당선, 반포역, 삼성역, 3));
        existSections.add(new Section(분당선, 삼성역, 역삼역, 2));
    }

    @DisplayName("첫구간을 추가한다.")
    @Test
    void add_firstSection() {
        //given
        Sections sections = new Sections();

        //when
        sections.add(new Section(신분당선, 강남역, 서초역, 10));

        //then
        assertEquals(1, sections.getSections().size());
    }

    @DisplayName("첫구간이 아닌 구간을 추가한다.")
    @Test
    void add_notFirstSection() {
        //given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 서초역, 10));

        //when
        sections.add(new Section(신분당선, 서초역, 역삼역, 5));

        //then
        assertAll(
                () -> assertEquals(2, sections.getSections().size()),
                () -> assertThat(sections.getSortedStation()).containsExactly(강남역, 서초역, 역삼역)
        );
    }

    @DisplayName("해당 구간들의 역들을 순서대로 구한다.")
    @Test
    void getSortedStations() {
        //given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 서초역, 2));
        sections.add(new Section(신분당선, 서초역, 반포역, 3));
        sections.add(new Section(신분당선, 반포역, 삼성역, 3));
        sections.add(new Section(신분당선, 삼성역, 역삼역, 2));

        //when
        List<Station> stations = sections.getSortedStation();

        //then
        assertThat(stations).containsExactly(강남역, 서초역, 반포역, 삼성역, 역삼역);
    }

    @DisplayName("추가하려는 구간 양역이 모두 구간들에 포함되어있으면 exception")
    @Test
    void add_fail_bothAlreadyAdded() {
        //when//then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> existSections.add(new Section(신분당선, 강남역, 서초역, 2)))
                .withMessage("both stations is already added.");
    }

    @DisplayName("추가하려는 구간 양역이 목두 구간등에 포함되지 않으면 exception")
    @Test
    void add_fail_bothNotContains() {
        //when//then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> existSections.add(new Section(신분당선, 안양역, 범계역, 2)))
                .withMessage("both stations is not contains.");
    }

    @DisplayName("구간들에서 첫번째역을 제거한다.")
    @Test
    void remove_first() {
        //when
        existSections.remove(강남역);

        //then
        assertAll(
                () -> assertEquals(3, existSections.getSections().size()),
                () -> assertThat(existSections.getSortedStation()).containsExactly(서초역, 반포역, 삼성역, 역삼역)
        );
    }

    @DisplayName("구간들에서 마지막역을 제거한다.")
    @Test
    void remove_last() {
        //when
        existSections.remove(역삼역);

        //then
        assertAll(
                () -> assertEquals(3, existSections.getSections().size()),
                () -> assertThat(existSections.getSortedStation()).containsExactly(강남역, 서초역, 반포역, 삼성역)
        );
    }

    @DisplayName("구간들에서 중간역을 제거한다.")
    @Test
    void remove_middle() {
        //when
        existSections.remove(서초역);

        //then
        assertAll(
                () -> assertEquals(3, existSections.getSections().size()),
                () -> assertThat(existSections.getSortedStation()).containsExactly(강남역, 반포역, 삼성역, 역삼역)
        );
    }

    @DisplayName("구간에 없는역을 제거하려고 하면 exception")
    @Test
    void remove_fail_noContainsStation() {
        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> existSections.remove(안양역))
                .withMessage("not contains station.");
    }

    @DisplayName("구간이 하나밖에없는데 제거하려고 하면 exception")
    @Test
    void remove_fail_sectionOnlyOne() {
        //given
        Sections sections = new Sections();
        sections.add(new Section(신분당선, 강남역, 서초역, 2));

        //when //then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> sections.remove(강남역))
                .withMessage("sections has only one.");
    }

}