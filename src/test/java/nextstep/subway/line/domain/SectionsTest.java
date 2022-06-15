package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class SectionsTest {
    private Line line;
    private Station upStation;
    private Station downStation;
    private Section section;
    private Sections sections;

    @BeforeEach
    void setUp() {
        // given
        this.line = Line.of("2호선", "GREEN");
        this.upStation = Station.from("강남역");
        this.downStation = Station.from("선릉역");
        this.section = Section.of(this.line, this.upStation, this.downStation, 20);
        this.sections = Sections.from(new ArrayList<>(Arrays.asList(this.section)));
    }

    @DisplayName("비어있는 지하철 구간 목록을 생성한다.")
    @Test
    void create_empty_sections() {
        // given & when
        Sections emptySections = Sections.empty();

        // then
        assertAll(
                () -> assertThat(emptySections).isNotNull(),
                () -> assertThat(emptySections.get()).hasSize(0)
        );
    }

    @DisplayName("지하철 구간 목록을 생성한다.")
    @Test
    void create_sections() {
        // when
        Sections newSections = Sections.from(Collections.singletonList(this.section));

        // then
        assertAll(
                () -> assertThat(newSections).isNotNull(),
                () -> assertThat(newSections.get()).hasSize(1)
        );
    }

    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void add_section() {
        // given
        Station addedStation = Station.from("역삼역");
        Section addedSection = Section.of(this.line, this.upStation, addedStation, 5);

        // when
        this.sections.add(addedSection);

        // then
        assertAll(
                () -> assertThat(this.sections.get()).hasSize(2),
                () -> assertThat(this.sections.getStations()).containsExactly(addedStation)
        );
    }

    @DisplayName("지하철 구간에서 지하철역을 제거한다.")
    @Test
    void remove_station() {
        // given
        Station addedStation = Station.from("역삼역");
        Section addedSection = Section.of(this.line, this.upStation, addedStation, 5);
        this.sections.add(addedSection);

        // when
        this.sections.removeStation(addedStation, this.line);

        // then
        assertAll(
                () -> assertThat(this.sections.get()).hasSize(1),
                () -> assertThat(this.sections.getStations()).doesNotContain(addedStation)
        );
    }

    @DisplayName("지하철 구간에 포함된 지하철역 목록을 조회한다.")
    @Test
    void get_stations() {
        // when & then
        assertAll(
                () -> assertThat(this.sections.getStations()).hasSize(2),
                () -> assertThat(this.sections.getStations()).containsExactly(this.upStation, this.downStation)
        );
    }
}