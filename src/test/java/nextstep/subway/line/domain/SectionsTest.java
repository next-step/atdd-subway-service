package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

        sections = Sections.from(new ArrayList<>(
            Arrays.asList(
                Section.of(1L, 신당, DDP, 10),
                Section.of(2L, 왕십리, 신당, 10),
                Section.of(3L, DDP, 을지로, 10)
            ))
        );
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

    @DisplayName("중간 종점 추가")
    @Test
    void addNewSection() {
        // given
        Station 상왕십리 = Station.of(5L, "상왕십리");
        Section section = Section.of(4L, 왕십리, 상왕십리, 5);

        // when
        sections.addNewSection(section);

        // then
        assertThat(sections.toStations().getList()).containsExactly(왕십리, 상왕십리, 신당, DDP, 을지로);
        assertThat(sections.findHasUpStation(왕십리).get().hasDistance(Distance.of(5))).isTrue();
        assertThat(sections.findHasDownStation(신당).get().hasDistance(Distance.of(5))).isTrue();
    }

    @DisplayName("상행 종점 추가")
    @Test
    void addNewSection_upStation() {
        // given
        Station 청량리 = Station.of(5L, "청량리");
        Section section = Section.of(4L, 청량리, 왕십리, 10);

        // when
        sections.addNewSection(section);

        // then
        assertThat(sections.toStations().getList()).containsExactly(청량리, 왕십리, 신당, DDP, 을지로);
        assertThat(sections.findHasUpStation(청량리).get().hasDistance(Distance.of(10))).isTrue();
    }

    @DisplayName("하행 종점 추가")
    @Test
    void addNewSection_downStation() {
        // given
        Station 시청 = Station.of(5L, "시청");
        Section section = Section.of(4L, 을지로, 시청, 10);

        // when
        sections.addNewSection(section);

        // then
        assertThat(sections.toStations().getList()).containsExactly(왕십리, 신당, DDP, 을지로, 시청);
        assertThat(sections.findHasDownStation(시청).get().hasDistance(Distance.of(10))).isTrue();
    }

    @DisplayName("상행 종점 제거")
    @Test
    void removeStation_upStation() {
        // when
        sections.removeStation(왕십리);

        // then
        assertThat(sections.toStations().getList()).containsExactly(신당, DDP, 을지로);
    }

    @DisplayName("하행 종점 제거")
    @Test
    void removeStation_downStation() {
        // when
        sections.removeStation(을지로);

        // then
        assertThat(sections.toStations().getList()).containsExactly(왕십리, 신당, DDP);
    }

    @DisplayName("중간 종점 제거")
    @Test
    void removeStation() {
        // when
        sections.removeStation(신당);

        // then
        assertThat(sections.toStations().getList()).containsExactly(왕십리, DDP, 을지로);
        assertThat(sections.findHasUpStation(왕십리).get().hasDistance(Distance.of(20))).isTrue();
    }
}
