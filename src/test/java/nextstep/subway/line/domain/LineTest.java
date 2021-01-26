package nextstep.subway.line.domain;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("NonAsciiCharacters")
public class LineTest {

    private Line line;
    Station 강남;
    Station 양재;
    Station 광교;

    @BeforeEach
    void setup() {
        line = new Line();
        강남 = new Station("강남");
        양재 = new Station("양재");
        광교 = new Station("광교");
    }

    @DisplayName("지하철 구간을 등록한다")
    @Test
    void addSection() {
        // given
        line.addSection(양재, 광교, 10);

        // when
        List<Station> stations = line.getStations();

        // then
        assertAll(
                () -> assertThat(stations).containsExactly(양재, 광교),
                () -> assertThat(stations).hasSize(2)
        );
    }

    @DisplayName("2개의 지하철 구간 중 역을 삭제한다")
    @Test
    void removeSection() {
        // given
        line.addSection(양재, 광교, 10);
        line.addSection(광교, 강남, 10);

        // when
        line.removeSection(광교);
        List<Station> stations = line.getStations();

        // then
        assertThat(stations).hasSize(2);
    }

    @DisplayName("1개의 지하철 구간 중 역을 삭제한다")
    @Test
    void removeSectionException() {
        // when
        line.addSection(양재, 광교, 10);

        // then
        assertThrows(RuntimeException.class, () -> line.removeSection(광교));
    }
}
