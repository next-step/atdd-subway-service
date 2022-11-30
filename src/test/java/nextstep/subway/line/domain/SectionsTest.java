package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.line.domain.Sections.ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE;
import static nextstep.subway.line.domain.Sections.NOT_EXIST_EXCEPTION_MESSAGE;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class SectionsTest {

    @DisplayName("A-B 구간일 경우 A역 B역을 반환한다.")
    @Test
    void add() {

        Sections sections = new Sections();
        sections.add(sectionAB());

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(1),
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB())
        );
    }

    @DisplayName("A-B-C 구간일 경우 A역 B역 C역을 반환한다.")
    @Test
    void addSection() {

        Sections sections = new Sections();
        sections.add(sectionAB());
        sections.add(sectionBC());

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(2),
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB(), stationC())
        );
    }

    @DisplayName("구간 역 목록 조회 시 하나도 없을 경우 emptyList 를 반환한다.")
    @Test
    void getStation() {

        Sections sections = new Sections();

        assertThat(sections.getStations()).isEqualTo(Collections.emptyList());
    }

    @DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없다.")
    @Test
    void add_fail_exist() {

        Sections sections = new Sections();
        sections.add(sectionAB());
        sections.add(sectionBC());

        assertThatThrownBy(() -> sections.add(sectionBC()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE);
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없다.")
    @Test
    void add_fail_not_exist() {

        Sections sections = new Sections();
        sections.add(sectionAB());

        assertThatThrownBy(() -> sections.add(sectionCD()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(NOT_EXIST_EXCEPTION_MESSAGE);
    }
}
