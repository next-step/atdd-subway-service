package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {
    private Station stationA;
    private Station stationB;
    private Station stationC;
    private Station stationD;
    private Station stationE;
    private Station stationF;
    private Sections sections;

    @BeforeEach
    void setUp() {
        stationA = new Station("A");
        ReflectionTestUtils.setField(stationA, "id", 1L);
        stationB = new Station("B");
        ReflectionTestUtils.setField(stationB, "id", 2L);
        stationC = new Station("C");
        ReflectionTestUtils.setField(stationC, "id", 3L);
        stationD = new Station("D");
        ReflectionTestUtils.setField(stationD, "id", 4L);
        stationE = new Station("E");
        ReflectionTestUtils.setField(stationE, "id", 5L);
        stationF = new Station("F");
        ReflectionTestUtils.setField(stationF, "id", 6L);
        sections = new Sections(new ArrayList<>(Arrays.asList(
                new Section(stationC, stationD, 10),
                new Section(stationB, stationC, 10),
                new Section(stationA, stationB, 10),
                new Section(stationD, stationE, 10)
        )));
    }

    @Test
    void addSection() {
        // given
        Sections sections = new Sections();
        Section section = new Section(new Station("강남역"), new Station("신도림역"), 10);
        Section section2 = new Section(new Station("강남역"), new Station("낙성대역"), 2);
        Section section3= new Section(new Station("낙성대역"), new Station("대림역"), 2);
        // when
        sections.add(section);
        sections.add(section2);
        sections.add(section3);
        // then
        assertAll(() -> {
            assertThat(sections.asList()).hasSize(3);
            assertThat(sections.asList().get(1).findUpStationName()).isEqualTo("강남역");
            assertThat(sections.asList().get(1).findDownStationName()).isEqualTo("낙성대역");
            assertThat(sections.asList().get(2).findUpStationName()).isEqualTo("낙성대역");
            assertThat(sections.asList().get(2).findDownStationName()).isEqualTo("대림역");
            assertThat(sections.asList().get(0).findUpStationName()).isEqualTo("대림역");
            assertThat(sections.asList().get(0).findDownStationName()).isEqualTo("신도림역");
        });
    }

    @Test
    @DisplayName("상행 종점에 추가")
    void addSectionEndPoint_up() {
        // given
        Section section = new Section(stationF, stationA, 5);
        // when
        sections.add(section);
        int expectDistance = sections.asList().get(sections.asList().size() - 1).getDistance();
        List<Station> actual = sections.getSortedStations();
        // then
        assertAll(() -> {
            assertThat(actual.get(0).getName()).isEqualTo("F");
            assertThat(expectDistance).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("하행 종점에 추가")
    void addSectionEndPoint_down() {
        // given
        Section section = new Section(stationE, stationF, 5);
        // when
        sections.add(section);
        int expectDistance = sections.asList().get(sections.asList().size() - 1).getDistance();
        List<Station> actual = sections.getSortedStations();
        // then
        assertAll(() -> {
            assertThat(actual.get(actual.size() - 1).getName()).isEqualTo("F");
            assertThat(expectDistance).isEqualTo(5);
        });
    }

    @Test
    @DisplayName("중간지점에 추가")
    void addSectionIntermediate() {
        // given
        Section section = new Section(stationC, stationF, 6);
        // when
        sections.add(section);
        Section sectionA = sections.asList().stream().filter(eachSection -> eachSection.isSameUpStation(stationC)).findFirst().get();
        Section sectionB = sections.asList().stream().filter(eachSection -> eachSection.isSameUpStation(stationF)).findFirst().get();
        // then
        assertAll(() -> {
            assertThat(sectionA.getDistance()).isEqualTo(6);
            assertThat(sectionB.getDistance()).isEqualTo(4);
        });
    }

    @Test
    @DisplayName("section 추가 시 상하행역이 기존 Section과 모두 동일하면 예외 발생 - 하행종점")
    void shouldExceptionWhenAddSameSection_endDownPoint() {
        // when // then
        assertThatThrownBy(() -> sections.add(new Section(stationD, stationE, 10))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("section 추가 시 상하행역이 기존 Section과 모두 동일하면 예외 발생 - 상행종점")
    void shouldExceptionWhenAddSameSection_endUpPoint() {
        // when // then
        assertThatThrownBy(() -> sections.add(new Section(stationA, stationB, 10))).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("section 추가 시 상하행역이 기존 Section과 모두 동일하면 예외 발생")
    @Test
    void shouldExceptionWhenUpDownStationAlreadyBeing() {
        // given
        Sections sections = new Sections();
        Section section = new Section(new Station("강남역"), new Station("보라매역"), 10);
        sections.add(section);
        // when // then
        assertThatThrownBy(() -> sections.add(section))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상행역 부터 하행역 순으로 정렬 확인.")
    @Test
    void sortedStations() {
        //when
        List<Station> actual = sections.getSortedStations();

        //then
        assertAll(() -> {
            assertThat(actual).hasSize(5);
            assertThat(actual.get(0)).isEqualTo(stationA);
            assertThat(actual.get(1)).isEqualTo(stationB);
            assertThat(actual.get(2)).isEqualTo(stationC);
            assertThat(actual.get(3)).isEqualTo(stationD);
            assertThat(actual.get(4)).isEqualTo(stationE);
        });
    }

    @DisplayName("station 삭제 시 해당 구간 삭제 확인")
    @Test
    void removeStation() {
        // when
        sections.removeStation(stationD);
        List<Station> actual = sections.getSortedStations();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(4);
            assertThat(actual.get(0)).isEqualTo(stationA);
            assertThat(actual.get(1)).isEqualTo(stationB);
            assertThat(actual.get(2)).isEqualTo(stationC);
            assertThat(actual.get(3)).isEqualTo(stationE);
        });
    }

}
