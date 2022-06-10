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
        list.add(new Section(null, A역, B역, 10));
        list.add(new Section(null, B역, C역, 10));
        list.add(new Section(null, C역, D역, 10));
    }

    @DisplayName("구간 내 상행 종점역 구하기")
    @Test
    void getLineUpStation() {
        // given
        Sections sections = new Sections(list);

        // when
        Station lineUpStation = sections.getLineUpStation();

        // then
        assertThat(lineUpStation).isEqualTo(A역);
    }

    @DisplayName("구간 내 하행 종점역 구하기")
    @Test
    void getLineDownStation() {
        // given
        Sections sections = new Sections(list);

        // when
        Station lineDownStation = sections.getLineDownStation();

        // then
        assertThat(lineDownStation).isEqualTo(D역);
    }

    @DisplayName("상행 종점역으로 구간 구하기")
    @Test
    void findSectionWithUpStation() {
        // given
        Sections sections = new Sections(list);

        // when
        Section section = sections.findSectionWithUpStation(A역).get();

        // then
        assertThat(section.getUpStation()).isEqualTo(A역);
        assertThat(section.getDownStation()).isEqualTo(B역);
    }

    @DisplayName("하행 종점역으로 구간 구하기")
    @Test
    void findSectionWithDownStation() {
        // given
        Sections sections = new Sections(list);

        // when
        Section section = sections.findSectionWithDownStation(D역).get();

        // then
        assertThat(section.getUpStation()).isEqualTo(C역);
        assertThat(section.getDownStation()).isEqualTo(D역);
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
}
