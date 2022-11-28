package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {
    public static final Line LINE = new Line("2호선", "bg-blue-400", new Station("강남역"), new Station("잠실역"), 10);

    @Test
    @DisplayName("line 수정")
    void update() {
        // when
        LINE.update(new Line("2호선", "bg-red-400"));
        //then
        assertThat(LINE.getColor()).isEqualTo("bg-red-400");
    }

    @Test
    @DisplayName("section 추가")
    void addSection() {
        // given
        Section newSection = new Section(LINE, new Station("강남역"), new Station("신림역"), 5);
        // when
        LINE.addSection(newSection);
        // then
        assertThat(LINE.getSections()).contains(newSection);
    }

    @Test
    @DisplayName("station 삭제")
    void deleteStation() {
        // given
        Section newSection = new Section(LINE, new Station("강남역"), new Station("신림역"), 5);
        LINE.addSection(newSection);
        // when
        LINE.deleteStation(new Station("신림역"));
        //
        assertThat(LINE.getSortedStations()).doesNotContain(new Station("신림역"));
        assertThat(LINE.getSortedStations()).hasSize(2);
    }
}
