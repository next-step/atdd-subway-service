package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("Sections 내 stations 을 조회한다.")
    void getStationsFromSections() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Section initSection = new Section(null, 상행종점역, 하행종점역, 10);

        // when
        Sections sections = new Sections(initSection);

        // then
        assertThat(sections.getStations()).containsExactly(상행종점역, 하행종점역);
    }

    @Test
    @DisplayName("Sections에 Section을 추가한다.")
    void addSectionSections() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Station 새로운역 = new Station("새로운역");
        Section initSection = new Section(null, 상행종점역, 하행종점역, 10);
        Sections sections = new Sections(initSection);

        // when
        Section addSection = new Section(null, 상행종점역, 새로운역, 8);
        sections.addSection(addSection);

        // then
        assertThat(sections.getStations()).containsExactly(상행종점역, 새로운역, 하행종점역);
    }

    @Test
    @DisplayName("Sections에 Station을 삭제한다.")
    void removeStationFromSections() {
        // given
        Station 상행종점역 = new Station("상행종점역");
        Station 하행종점역 = new Station("하행종점역");
        Station 새로운역 = new Station("새로운역");
        Section initSection = new Section(null, 상행종점역, 하행종점역, 10);
        Sections sections = new Sections(initSection);
        Section addSection = new Section(null, 상행종점역, 새로운역, 8);
        sections.addSection(addSection);

        // when
        sections.removeStation(상행종점역);

        // then
        assertThat(sections.getStations()).containsExactly(새로운역, 하행종점역);
    }

}
