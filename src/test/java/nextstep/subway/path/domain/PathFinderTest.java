package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.station.domain.Station;

class PathFinderTest {
    Station 왕십리;
    Station 신당;
    Station 행당;
    Station 청구;
    Station DDP;

    Lines lines;

    @BeforeEach
    void setup() {
        왕십리 = Station.of(1L, "왕십리");
        신당 = Station.of(2L, "신당");
        행당 = Station.of(3L, "행당");
        청구 = Station.of(4L, "청구");
        DDP = Station.of(5L, "DDP");

        Line LINE_2 = new Line("2호선", "green", 왕십리, DDP, 20);
        LINE_2.addLineStation(왕십리, 신당, 10);
        Line LINE_5 = new Line("2호선", "purple", 왕십리, DDP, 30);
        LINE_5.addLineStation(왕십리, 행당, 10);
        LINE_5.addLineStation(행당, 청구, 10);

        lines = Lines.from(Arrays.asList(LINE_2, LINE_5));
    }

    @Test
    void 경로_조회_성공() {
        // given
        Path expected = Path.of(
            Stations.from(Arrays.asList(왕십리, 신당, DDP)),
            Distance.from(20)
        );

        // when
        Path actual = new PathFinder(lines).findPath(왕십리, DDP);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 경로_조회_출발역_도착역_동일() {
        assertThatThrownBy(() -> new PathFinder(lines).findPath(왕십리, 왕십리))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 경로_조회_도달_불가() {
        Station 대구역 = Station.of(6L, "대구역");

        assertThatThrownBy(() -> new PathFinder(lines).findPath(왕십리, 대구역))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
