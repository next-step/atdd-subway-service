package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static nextstep.subway.station.StationFixture.stationA;
import static nextstep.subway.station.StationFixture.stationB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {

    @DisplayName("A-B 구간일 경우 A역 B역을 반환한다.")
    @Test
    void add() {

        Sections sections = new Sections();
        sections.add(SectionFixture.sectionAB());
        sections.add(SectionFixture.sectionBC());

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(2),
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB())
        );
    }

    @DisplayName("구간 역 목록 조회 시 하나도 없을 경우 emptyList 를 반환한다.")
    @Test
    void getStation() {

        Sections sections = new Sections();

        assertThat(sections.getStations()).isEqualTo(Collections.emptyList());
    }
}
