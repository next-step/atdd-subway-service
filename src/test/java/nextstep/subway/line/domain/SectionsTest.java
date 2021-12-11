package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    @Test
    void create() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Station station3 = new Station("선릉역");
        final Section section1 = new Section(station1, station2, 1);
        final Section section2 = new Section(station2, station3, 1);

        // when
        final Sections sections = new Sections(Arrays.asList(section1, section2));

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(2)
        );
    }

    @Test
    void add() {
        // given
        final Sections sections = new Sections(Collections.emptyList());

        // when
        final Section newSection = new Section(

            new Station("강남역"),
            new Station("역삼역"),
            1
        );
        sections.add(newSection);

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(1)
        );
    }

    @Test
    void add_prepend() {
        // given
        final Section firstSection = new Section(

            new Station("강남역"),
            new Station("역삼역"),
            1
        );
        final Sections sections = new Sections(Arrays.asList(firstSection));

        // when
        final Section newSection = new Section(

            new Station("교대역"),
            new Station("강남역"),
            1
        );
        sections.add(newSection);

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(2)
        );
    }

    @Test
    void add_append() {
        // given
        final Section lastSection = new Section(

            new Station("강남역"),
            new Station("역삼역"),
            1
        );
        final Sections sections = new Sections(Arrays.asList(lastSection));

        // when
        final Section newSection = new Section(

            new Station("역삼역"),
            new Station("선릉역"),
            1
        );
        sections.add(newSection);

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(2)
        );
    }

    @Test
    void add_inBetween_matchingUpStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station1, station3, 1);
        sections.add(newSection);

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(2)
        );
    }

    @Test
    void add_inBetween_matchingUpStation_invalidDistance() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station1, station3, 1);

        // then
        assertThatThrownBy(
            () -> sections.add(newSection)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_inBetween_matchingDownStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station3, station2, 1);
        sections.add(newSection);

        // then
        assertAll(
            () -> assertThat(sections).isNotNull(),
            () -> assertThat(sections.size()).isEqualTo(2)
        );
    }

    @Test
    void add_inBetween_matchingDownStation_invalidDistance() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station3, station2, 1);

        // then
        assertThatThrownBy(
            () -> sections.add(newSection)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_existingSection() {
        // given
        final Section section = new Section(

            new Station("강남역"),
            new Station("역삼역"),
            2
        );
        final Sections sections = new Sections(Arrays.asList(section));

        // when, then
        assertThatThrownBy(
            () -> sections.add(section)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_noMatchingStations() {
        // given
        final Section section = new Section(

            new Station("강남역"),
            new Station("역삼역"),
            2
        );
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Section newSection = new Section(

            new Station("판교역"),
            new Station("광교역"),
            2
        );

        // then
        assertThatThrownBy(
            () -> sections.add(newSection)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getStationsInOrder_prepend() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("교대역");
        final Section newSection = new Section(station3, station1, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(station3),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(station1),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(station2)
        );
    }

    @Test
    void getStationsInOrder_append() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("선릉역");
        final Section newSection = new Section(station2, station3, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(station1),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(station2),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(station3)
        );
    }

    @Test
    void getStationsInOrder_inBetween_matchingUpStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station1, station3, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(station1),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(station3),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(station2)
        );
    }

    @Test
    void getStationsInOrder_inBetween_matchingDownStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("역삼역");
        final Section section = new Section(station1, station2, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station station3 = new Station("테헤란역");
        final Section newSection = new Section(station3, station2, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(station1),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(station3),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(station2)
        );
    }

    @Test
    void getStationsInOrder_empty() {
        // given
        final Sections sections = new Sections(Collections.emptyList());

        // when
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertThat(stationsInOrder).isEmpty();
    }

    @Test
    void deleteStation_firstStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("판교역");
        final Station station3 = new Station("광교역");
        final Section section1 = new Section(station1, station2, 4);
        final Section section2 = new Section(station2, station3, 8);
        final Sections sections = new Sections(Arrays.asList(section1, section2));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(station1);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void deleteStation_lastStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("판교역");
        final Station station3 = new Station("광교역");
        final Section section1 = new Section(station1, station2, 4);
        final Section section2 = new Section(station2, station3, 8);
        final Sections sections = new Sections(Arrays.asList(section1, section2));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(station3);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void deleteStation_inBetween() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("판교역");
        final Station station3 = new Station("광교역");
        final Section section1 = new Section(station1, station2, 4);
        final Section section2 = new Section(station2, station3, 8);
        final Sections sections = new Sections(Arrays.asList(section1, section2));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(station2);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void deleteStation_emptyLine() {
        // given
        final Sections sections = new Sections(Collections.emptyList());
        final Station unknownStation = new Station("광교역");

        // when, then
        assertThatThrownBy(
            () -> sections.deleteStation(unknownStation)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteStation_oneSection() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("판교역");
        final Section section = new Section(station1, station2, 4);
        final Sections sections = new Sections(Arrays.asList(section));

        // when, then
        assertThatThrownBy(
            () -> sections.deleteStation(station1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteStation_unknownStation() {
        // given
        final Station station1 = new Station("강남역");
        final Station station2 = new Station("판교역");
        final Section section = new Section(station1, station2, 4);
        final Sections sections = new Sections(Arrays.asList(section));
        final Station unknownStation = new Station("광교역");

        // when, then
        assertThatThrownBy(
            () -> sections.deleteStation(unknownStation)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
