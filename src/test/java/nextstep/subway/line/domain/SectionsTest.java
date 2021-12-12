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
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Station 선릉역 = new Station("선릉역");
        final Section 강남_역삼_구간 = new Section(강남역, 역삼역, 1);
        final Section 역삼_선릉_구간 = new Section(역삼역, 선릉역, 1);

        // when
        final Sections sections = new Sections(Arrays.asList(강남_역삼_구간, 역삼_선릉_구간));

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
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section 강남_역삼_구간 = new Section(강남역, 역삼역, 2);
        final Sections sections = new Sections(Arrays.asList(강남_역삼_구간));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(강남역, 테헤란역, 1);
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
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(강남역, 테헤란역, 1);

        // then
        assertThatThrownBy(
            () -> sections.add(newSection)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void add_inBetween_matchingDownStation() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(테헤란역, 역삼역, 1);
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
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(테헤란역, 역삼역, 1);

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
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 교대역 = new Station("교대역");
        final Section newSection = new Section(교대역, 강남역, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(교대역),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(강남역),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(역삼역)
        );
    }

    @Test
    void getStationsInOrder_append() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 1);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 선릉역 = new Station("선릉역");
        final Section newSection = new Section(역삼역, 선릉역, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(강남역),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(역삼역),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(선릉역)
        );
    }

    @Test
    void getStationsInOrder_inBetween_matchingUpStation() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(강남역, 테헤란역, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(강남역),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(테헤란역),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(역삼역)
        );
    }

    @Test
    void getStationsInOrder_inBetween_matchingDownStation() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 역삼역 = new Station("역삼역");
        final Section section = new Section(강남역, 역삼역, 2);
        final Sections sections = new Sections(Arrays.asList(section));

        // when
        final Station 테헤란역 = new Station("테헤란역");
        final Section newSection = new Section(테헤란역, 역삼역, 1);
        sections.add(newSection);
        final List<Station> stationsInOrder = sections.getStationsInOrder();

        // then
        assertAll(
            () -> assertThat(stationsInOrder.get(0)).isEqualTo(강남역),
            () -> assertThat(stationsInOrder.get(1)).isEqualTo(테헤란역),
            () -> assertThat(stationsInOrder.get(2)).isEqualTo(역삼역)
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
        final Station 강남역 = new Station("강남역");
        final Station 판교역 = new Station("판교역");
        final Station 광교역 = new Station("광교역");
        final Section 강남_판교_구간 = new Section(강남역, 판교역, 4);
        final Section 판교_광교_구간 = new Section(판교역, 광교역, 8);
        final Sections sections = new Sections(Arrays.asList(강남_판교_구간, 판교_광교_구간));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(강남역);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void deleteStation_lastStation() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 판교역 = new Station("판교역");
        final Station 광교역 = new Station("광교역");
        final Section 강남_판교_구간 = new Section(강남역, 판교역, 4);
        final Section 판교_광교_구간 = new Section(판교역, 광교역, 8);
        final Sections sections = new Sections(Arrays.asList(강남_판교_구간, 판교_광교_구간));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(광교역);

        // then
        assertThat(sections.size()).isEqualTo(1);
    }

    @Test
    void deleteStation_inBetween() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 판교역 = new Station("판교역");
        final Station 광교역 = new Station("광교역");
        final Section 강남_판교_구간 = new Section(강남역, 판교역, 4);
        final Section 판교_광교_구간 = new Section(판교역, 광교역, 8);
        final Sections sections = new Sections(Arrays.asList(강남_판교_구간, 판교_광교_구간));
        assertThat(sections.size()).isEqualTo(2);

        // when
        sections.deleteStation(판교역);

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
        final Station 강남역 = new Station("강남역");
        final Station 판교역 = new Station("판교역");
        final Section section = new Section(강남역, 판교역, 4);
        final Sections sections = new Sections(Arrays.asList(section));

        // when, then
        assertThatThrownBy(
            () -> sections.deleteStation(강남역)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteStation_unknownStation() {
        // given
        final Station 강남역 = new Station("강남역");
        final Station 판교역 = new Station("판교역");
        final Section section = new Section(강남역, 판교역, 4);
        final Sections sections = new Sections(Arrays.asList(section));
        final Station unknownStation = new Station("광교역");

        // when, then
        assertThatThrownBy(
            () -> sections.deleteStation(unknownStation)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
