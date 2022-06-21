package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    private Line line;

    @BeforeEach
    void setUp() {
        line = new Line("2호선", "bg-blue-400", new Station("강남역"), new Station("잠실역"), 10);
    }

    @Test
    @DisplayName("line 수정")
    void update() {
        // when
        line.update(new Line("2호선", "bg-red-400"));
        //then
        assertThat(line.getColor()).isEqualTo("bg-red-400");
    }

    @Test
    @DisplayName("section 추가")
    void addSection() {
        // given
        Section newSection = new Section(line, new Station("강남역"), new Station("신림역"), 5);
        // when
        line.addSection(newSection);
        // then
        assertThat(line.getSections()).contains(newSection);
    }

    @Test
    @DisplayName("station 삭제")
    void deleteStation() {
        // given
        Section newSection = new Section(line, new Station("강남역"), new Station("신림역"), 5);
        line.addSection(newSection);
        // when
        line.deleteStation(new Station("신림역"));
        //
        assertThat(line.getSortedStations()).doesNotContain(new Station("신림역"));
        assertThat(line.getSortedStations()).hasSize(2);
    }
}
