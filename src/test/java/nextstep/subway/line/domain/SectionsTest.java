package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {
    private Station A역;
    private Station B역;
    private Station C역;
    private Station D역;
    private Station E역;
    private Station F역;
    private List<Section> list;

    @BeforeEach
    void setUp() {
        A역 = new Station(1L, "A역");
        B역 = new Station(2L, "B역");
        C역 = new Station(3L, "C역");
        D역 = new Station(4L, "D역");
        E역 = new Station(5L, "E역");
        F역 = new Station(6L, "F역");

        list = new ArrayList<>();
        list.add(new Section(A역, B역, 10));
        list.add(new Section(B역, C역, 10));
        list.add(new Section(C역, D역, 10));
    }

    @DisplayName("지하철 역 리스트 구하기")
    @Test
    void getStations() {
        // given
        Sections sections = new Sections(list);

        // when
        List<Station> stations = sections.getStations();

        // then
        assertThat(stations).containsExactly(A역, B역, C역, D역);
    }

    @DisplayName("구간 추가하기")
    @Test
    void insertSection() {
        // given
        List<Section> list = new ArrayList<>();
        list.add(new Section(B역, E역, 10));
        Sections sections = new Sections(list);
        
        // when
        sections.insertSection(null, new Section(A역, B역, 10));
        sections.insertSection(null, new Section(B역, C역, 3));
        sections.insertSection(null, new Section(D역, E역, 3));
        sections.insertSection(null, new Section(E역, F역, 10));
        
        // then
        assertThat(sections.getList()).hasSize(5);
    }

    @DisplayName("구간 삭제하기")
    @Test
    void deleteStation() {
        // given
        List<Section> list = new ArrayList<>();
        list.add(new Section(B역, E역, 10));
        Sections sections = new Sections(list);
        sections.insertSection(null, new Section(A역, B역, 10));
        sections.insertSection(null, new Section(B역, C역, 3));
        sections.insertSection(null, new Section(D역, E역, 3));
        sections.insertSection(null, new Section(E역, F역, 10));

        // when
        sections.deleteStation(null, A역);
        sections.deleteStation(null, F역);
        sections.deleteStation(null, C역);
        sections.deleteStation(null, D역);

        // then
        assertThat(sections.getList()).hasSize(1);
    }
}
