package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.domain.LineFixture.lineA;
import static nextstep.subway.line.domain.SectionFixture.sectionBC;
import static nextstep.subway.line.domain.Sections.MININUM_SECTIONS_SIZE_EXCEPTION_MESSAGE;
import static nextstep.subway.station.StationFixture.stationA;
import static nextstep.subway.station.StationFixture.stationD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 관련 기능")
public class LineTest {

    @DisplayName("노선에 등록 되어있지 않은 역을 제거할 수 없다.")
    @Test
    void remove_fail_exist() {
        Line line = lineA();
        line.add(sectionBC());

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
                .hasMessageContaining(MININUM_SECTIONS_SIZE_EXCEPTION_MESSAGE);
    }
}
