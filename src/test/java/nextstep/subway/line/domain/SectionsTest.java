package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static nextstep.subway.line.domain.LineFixture.lineA;
import static nextstep.subway.line.domain.LineFixture.lineB;
import static nextstep.subway.line.domain.Section.SECTION_DISTANCE_EXCEPTION_MESSAGE;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.line.domain.Sections.ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE;
import static nextstep.subway.line.domain.Sections.NOT_EXIST_EXCEPTION_MESSAGE;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("구간 관련 기능")
public class SectionsTest {

    @DisplayName("A-B 구간일 경우 A역 B역을 반환한다.")
    @Test
    void add() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionAB(line));

        assertAll(
                () -> assertThat(sections.size()).isEqualTo(1),
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB())
        );
    }

    @DisplayName("A-B-C 구간일 경우 A역 B역 C역을 반환한다.")
    @Test
    void addSection() {

        Line line = lineA();

        line.getSections().add(sectionBC(line));

        assertAll(
                () -> assertThat(line.getSections().size()).isEqualTo(2),
                () -> assertThat(line.getStations()).containsExactly(stationA(), stationB(), stationC())
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

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionBC(line));

        assertThatThrownBy(() -> sections.add(sectionBC(line)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(ALREADY_EXIST_SECTION_EXCEPTION_MESSAGE);
    }

    @DisplayName("상행역과 하행역 둘 중 하나도 포함되어있지 않으면 추가할 수 없다.")
    @Test
    void add_fail_not_exist() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionAB(line));

        assertThatThrownBy(() -> sections.add(sectionCD(line)))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(NOT_EXIST_EXCEPTION_MESSAGE);
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다. / A-B 구간에 B-C 구간을 추가한다.")
    @Test
    void addLastDownStation() {

        Line line = lineA();

        line.getSections().add(sectionBC(line));

        assertAll(
                () -> assertThat(line.getStations()).containsExactly(stationA(), stationB(), stationC()),
                () -> assertThat(line.getSections()).hasSize(2)
        );
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다. / B-C 구간에 A-B 구간을 추가한다.")
    @Test
    void addFirstUpStation() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionBC(line));
        sections.add(sectionAB(line));

        assertAll(
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB(), stationC()),
                () -> assertThat(sections.getSections()).hasSize(2)
        );
    }

    @DisplayName("구간 사이에 구간을 추가한다. / 상행역을 기준으로 구간을 추가한다. / A-C 구간에 A-B 구간 추가")
    @Test
    void addBetweenUpStation() {

        Line line = lineB();

        line.addSection(sectionAB(line));

        assertAll(
                () -> assertThat(line.getStations()).containsExactly(stationA(), stationB(), stationC()),
                () -> assertThat(line.getSections()).hasSize(2)
        );
    }

    @DisplayName("구간 사이에 구간을 추가한다. / 하행역을 기준으로 구간을 추가한다. / A-C 구간에 B-C 구간 추가")
    @Test
    void addBetweenDownStation() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionAC(line));
        sections.add(sectionBC(line));

        assertAll(
                () -> assertThat(sections.getStations()).containsExactly(stationA(), stationB(), stationC()),
                () -> assertThat(sections.getSections()).hasSize(2)
        );
    }

    @DisplayName("구간 사이에 구간을 추가한다. / 상행역을 기준으로 구간을 추가한다. / A-C 구간에 A-B 구간 추가 / A-B 구간의 거리가 A-C 구간의 거리보다 크거나 같으면 등록을 할 수 없다.")
    @Test
    void addBetweenUpStation_fail() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionAC(line));

        assertThatThrownBy(() -> sections.add(new Section(lineA(), stationA(), stationB(), new Distance(DISTANCE_A_C))))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(SECTION_DISTANCE_EXCEPTION_MESSAGE);
    }

    @DisplayName("구간 사이에 구간을 추가한다. / 하행역을 기준으로 구간을 추가한다. / A-C 구간에 B-C 구간 추가 / B-C 구간의 거리가 A-C 구간의 거리보다 크거나 같으면 등록을 할 수 없다.")
    @Test
    void addBetweenDownStation_fail() {

        Line line = lineA();

        Sections sections = new Sections();
        sections.add(sectionAC(line));

        assertThatThrownBy(() -> sections.add(new Section(lineA(), stationB(), stationC(), new Distance(DISTANCE_A_C))))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(SECTION_DISTANCE_EXCEPTION_MESSAGE);
    }
}
