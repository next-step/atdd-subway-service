package nextstep.subway.line.domain;

import nextstep.subway.exception.NotValidDataException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.exception.type.ValidExceptionType.ALREADY_EXIST_LINE_STATION;
import static nextstep.subway.exception.type.ValidExceptionType.SECTIONS_MIN_SIZE_ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class SectionsTest {
    private final Line line = Line.of("1호선", "red");
    private final Station stationA = Station.of("수원역");
    private final Station stationB = Station.of("성균관대역");
    private final Station stationC = Station.of("화서역");
    private final Distance distance = Distance.from(10);

    @Test
    @DisplayName("구간을 추가한다.")
    void addSection() {
        Sections sections = new Sections();
        sections.addSection(line, stationA, stationB, distance);
        sections.addSection(line, stationB, stationC, distance);

        assertThat(sections.getSections()).hasSize(2);
    }

    @Test
    @DisplayName("구간에 있는 역을 삭제한다.")
    void removeLineStation() {
        Sections sections = new Sections();
        sections.addSection(line, stationA, stationB, distance);
        sections.addSection(line, stationB, stationC, distance);

        sections.removeLineStation(line, stationA);
        assertThat(sections.getStations()).containsOnly(stationB, stationC);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    @DisplayName("구간에이 사이즈가 1보다 작거나 같으면 오류를 리턴한다.")
    void validCheckSectionSize() {
        Sections sections = new Sections();
        sections.addSection(line, stationA, stationB, distance);

        assertThatThrownBy(() -> {
            sections.removeLineStation(line, stationA);
        }).isInstanceOf(NotValidDataException.class)
                .hasMessageContaining(SECTIONS_MIN_SIZE_ONE.getMessage());
    }

    @Test
    @DisplayName("구간에 이미 등록된 역들이면 오류를 리턴한다.")
    void isExistBothStation() {
        Sections sections = new Sections();
        sections.addSection(line, stationA, stationB, distance);

        assertThatThrownBy(() -> {
            sections.addSection(line, stationA, stationB, distance);
        }).isInstanceOf(NotValidDataException.class)
                .hasMessageContaining(ALREADY_EXIST_LINE_STATION.getMessage());
    }
}