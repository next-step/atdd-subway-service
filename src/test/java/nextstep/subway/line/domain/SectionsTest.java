package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("구간 추가")
    void addSectionTest() {
        // given
        Sections sections = new Sections();
        Line 이호선 = new Line("2호선", "green");
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Section section = new Section(이호선, 강남역, 교대역, 10);

        // when
        sections.add(section);

        // then
        assertThat(sections.getStations()).hasSize(2);
    }
}