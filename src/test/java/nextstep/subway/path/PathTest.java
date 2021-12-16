package nextstep.subway.path;

import nextstep.subway.line.LineTestFixture;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathTest {
    private final Station upStation = mock(Station.class);
    private final Station downStation = mock(Station.class);
    private final Station addStation = mock(Station.class);

    @Test
    @DisplayName("지하철 최단 경로를 조회한다.")
    void findPathTest() {
        when(upStation.getId()).thenReturn(1L);
        when(downStation.getId()).thenReturn(2L);
        when(addStation.getId()).thenReturn(3L);
        Line line = LineTestFixture.노선을_생성한다("1호선", "red", upStation, downStation, 10);
        line.addStation(downStation, addStation, 5);
        PathFinder pathFinder = new PathFinder(line);
        assertThat(pathFinder.findPath(upStation.getId(), addStation.getId())).containsExactly(upStation.getId(), downStation.getId(), addStation.getId());
    }
}