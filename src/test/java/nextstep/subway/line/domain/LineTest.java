package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.line.domain.LineFixture.lineA;
import static nextstep.subway.line.domain.SectionFixture.sectionBC;
import static nextstep.subway.line.domain.Sections.MINIMUM_SECTIONS_SIZE_EXCEPTION_MESSAGE;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
public class LineTest {

    @DisplayName("노선 생성")
    @ParameterizedTest
    @CsvSource({"1호선, blue, 900"})
    void constructor(String name, String color, int fare) {
        Line line = Line.of(name, color, fare);
        assertAll(
                () -> assertThat(line.getColor()).isEqualTo(color),
                () -> assertThat(line.getName()).isEqualTo(name),
                () -> assertThat(line.getFare()).isEqualTo(fare)
        );
    }

    @DisplayName("노선에 등록 되어있지 않은 역을 제거할 수 없다.")
    @Test
    void remove_fail_exist() {
        Line line = lineA();
        line.add(sectionBC(line));

        line.removeLineStation(stationD());

        assertAll(
                () -> assertThat(line.getSections()).hasSize(2)
        );
    }

    @DisplayName("하나의 구간만 있을 경우 구간을 제거할 수 없다.")
    @Test
    void removeStation_fail_size() {
        Line line = lineA();

        assertThatThrownBy(() -> line.removeLineStation(stationA()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(MINIMUM_SECTIONS_SIZE_EXCEPTION_MESSAGE);
    }

    @DisplayName("A-B-C 구간의 노선에서 B역이 포함된 구간을 제거한다")
    @Test
    void removeBetweenStation() {
        Line line = lineA();
        line.add(sectionBC(line));

        line.removeLineStation(stationB());

        assertThat(line.getStations()).containsExactly(stationA(), stationC());
    }

    @DisplayName("A-B-C 구간의 노선에서 A역을 제거한다.")
    @Test
    void removeUpStation_success() {
        Line line = lineA();
        line.add(sectionBC(line));

        line.removeLineStation(stationA());

        assertThat(line.getStations()).containsExactly(stationB(), stationC());
    }

    @DisplayName("A-B-C 구간의 노선에서 C역을 제거한다.")
    @Test
    void removeDownStation_success() {
        Line line = lineA();
        line.add(sectionBC(line));

        line.removeLineStation(stationC());

        assertThat(line.getStations()).containsExactly(stationA(), stationB());
    }
}
