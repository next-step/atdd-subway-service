package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {
    Station 왕십리;
    Station 신당;
    Station DDP;
    Station 을지로;

    Sections sections;

    @BeforeEach
    void setup() {
        왕십리 = Station.of(1L, "왕십리");
        신당 = Station.of(2L, "신당");
        DDP = Station.of(3L, "DDP");
        을지로 = Station.of(4L, "을지로");

        sections = Sections.from(Arrays.asList(
            Section.of(1L, 신당, DDP),
            Section.of(2L, 왕십리, 신당),
            Section.of(3L, DDP, 을지로)
        ));
    }

    @Test
    void toStations() {
        // when
        Stations stations = sections.toStations();
        // then
        assertThat(stations.getList()).containsExactly(왕십리, 신당, DDP, 을지로);
    }

    @Test
    void findHasDownStation() {
        // when
        Section section = sections.findHasDownStation(DDP).orElse(null);

        // then
        assertThat(section).isNotNull();
        assertThat(section).isEqualTo(Section.of(1L, 신당, DDP));
    }

    @Test
    void findHasDownStation_notExists() {
        // when
        Section section = sections.findHasDownStation(왕십리).orElse(null);

        // then
        assertThat(section).isNull();
    }

    @Test
    void findHasUpStation() {
        // when
        Section section = sections.findHasUpStation(DDP).orElse(null);

        // then
        assertThat(section).isNotNull();
        assertThat(section).isEqualTo(Section.of(3L, DDP, 을지로));
    }

    @Test
    void findHasUpStation_notExists() {
        // when
        Section section = sections.findHasUpStation(을지로).orElse(null);

        // then
        assertThat(section).isNull();
    }
}
