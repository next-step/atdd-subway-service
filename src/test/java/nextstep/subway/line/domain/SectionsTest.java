package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionsTest {

    Station 강남역;
    Station 판교역;
    Station 정자역;
    Station 청계천역;
    Station 광교역;
    Line 신분당선;
    Section 강남판교;
    Section 판교정자;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        정자역 = new Station("정자역");
        청계천역 = new Station("청계천역");
        광교역 = new Station("광교역");
        신분당선 = new Line("신분당선", "bg-red-600");
        강남판교 = Section.of(신분당선, 강남역, 판교역, Distance.from(7));
        판교정자 = Section.of(신분당선, 판교역, 정자역, Distance.from(3));
    }

    @DisplayName("첫구간을 추가한다.")
    @Test
    void addFirstSection() {
        // given
        Sections sections = new Sections();

        // when
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));

        // then
        assertEquals(1, sections.getSections().size());
    }

    @DisplayName("첫구간이 아닌 구간을 추가한다.")
    @Test
    void addNotFirstSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));

        // when
        sections.addSection(Section.of(신분당선, 판교역, 정자역, Distance.from(5)));

        // then
        assertAll(
            () -> assertEquals(2, sections.getSections().size()),
            () -> assertThat(sections.stations()).contains(강남역, 판교역, 정자역)
        );
    }

    @DisplayName("추가하려는 구간에 속한 역이 모두 이미 있는 경우에는 예외가 발생해야 한다.")
    @Test
    void addExistSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));
        sections.addSection(Section.of(신분당선, 판교역, 정자역, Distance.from(5)));

        // then
        assertAll(
            () -> assertEquals(2, sections.getSections().size()),
            () -> assertThatThrownBy(
                () -> sections.addSection(Section.of(신분당선, 강남역, 정자역, Distance.from(15))))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("추가하려는 구간에 속한 역이 모두 없는 경우에는 예외가 발생해야 한다.")
    @Test
    void addNotExistSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));
        sections.addSection(Section.of(신분당선, 판교역, 정자역, Distance.from(5)));

        // then
        assertAll(
            () -> assertEquals(2, sections.getSections().size()),
            () -> assertThatThrownBy(
                () -> sections.addSection(Section.of(신분당선, 청계천역, 광교역, Distance.from(10))))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("두 역 사이에 새로운 역을 등록할 때, 기존의 두 역 거리 이상이면 예외가 발생해야 한다.")
    void addSectionWithInvalidDistance() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 정자역, Distance.from(10)));

        // when
        Section added = Section.of(신분당선, 강남역, 판교역, Distance.from(10));

        // then
        assertAll(
            () -> assertEquals(1, sections.getSections().size()),
            () -> assertThatThrownBy(
                () -> sections.addSection(added))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("상행역이 같은 구간을 등록한다.")
    void addSectionWithSameUpStation() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 정자역, Distance.from(10)));

        // when
        Section added = Section.of(신분당선, 강남역, 판교역, Distance.from(5));
        sections.addSection(added);

        // then
        List<Station> stations = sections.stations();
        List<String> stationNames = stations.stream()
            .map(Station::getName)
            .collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("강남역", "판교역", "정자역"));
    }

    @Test
    @DisplayName("하행역이 같은 구간을 등록한다.")
    void addSectionWithSameDownStation() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 정자역, Distance.from(10)));

        // when
        Section added = Section.of(신분당선, 정자역, 광교역, Distance.from(5));
        sections.addSection(added);

        // then
        List<Station> stations = sections.stations();
        List<String> stationNames = stations.stream()
            .map(Station::getName)
            .collect(Collectors.toList());
        assertThat(stationNames).containsAll(Arrays.asList("강남역", "정자역", "광교역"));
    }

    @Test
    @DisplayName("노선에서 중간역을 제거한다.")
    void removeSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));
        sections.addSection(Section.of(신분당선, 판교역, 정자역, Distance.from(5)));

        // when
        sections.deleteStation(판교역);

        // then
        assertAll(
            () -> assertEquals(1, sections.getSections().size()),
            () -> assertThat(sections.stations()).contains(강남역, 정자역)
        );
    }

    @Test
    @DisplayName("노선에 없는 역을 제거하면 예외를 반환해야 한다.")
    void removeNotExistStation() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));
        sections.addSection(Section.of(신분당선, 판교역, 정자역, Distance.from(5)));

        // when & then
        assertAll(
            () -> assertEquals(2, sections.getSections().size()),
            () -> assertThatThrownBy(
                () -> sections.deleteStation(광교역))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @Test
    @DisplayName("구간이 하나밖에 없는 노선에서 마지막 구간을 제거하면 예외를 반환해야 한다.")
    void removeOnlyOneSection() {
        // given
        Sections sections = new Sections();
        sections.addSection(Section.of(신분당선, 강남역, 판교역, Distance.from(10)));

        // when & then
        assertAll(
            () -> assertEquals(1, sections.getSections().size()),
            () -> assertThatThrownBy(
                () -> sections.deleteStation(판교역))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }
}
