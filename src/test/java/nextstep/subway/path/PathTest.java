package nextstep.subway.path;

import com.google.common.collect.Lists;
import nextstep.subway.line.LineTestFixture;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PathTest {
    private final Station upStationFirstLine = mock(Station.class);
    private final Station downStationFirstLine = mock(Station.class);
    private final Station addStationFirstLine = mock(Station.class);
    private final Station upStationSecondLine = mock(Station.class);
    private final Station downStationSecondLine = mock(Station.class);
    private final Station upStationThirdLine = mock(Station.class);
    private final Station downStationThirdLine = mock(Station.class);

    @Test
    @DisplayName("노선이 1개일때 지하철 최단 경로를 조회한다.")
    void findPathTestOneLine() {
        when(upStationFirstLine.getId()).thenReturn(1L);
        when(downStationFirstLine.getId()).thenReturn(2L);
        when(addStationFirstLine.getId()).thenReturn(3L);

        Line firstLine = LineTestFixture.노선을_생성한다("1호선", "red", upStationFirstLine, downStationFirstLine, 10);
        firstLine.addStation(downStationFirstLine, addStationFirstLine, 5);
        PathFinder pathFinder = new PathFinder(firstLine);
        assertThat(pathFinder.findPath(upStationFirstLine.getId(), addStationFirstLine.getId())).containsExactly(upStationFirstLine.getId(), downStationFirstLine.getId(), addStationFirstLine.getId());
    }


    @Test
    @DisplayName("노선이 여러개일때 지하철 최단 경로를 조회한다.")
    void findPathTestManyLine() {
        /**
         * 4L    --- *2호선* ---    1L
         * |                        |
         * *3호선*                   *1호선*
         * |                        |
         * 3L  --- *1호선* ---      2L
         */
        when(upStationFirstLine.getId()).thenReturn(1L);
        when(downStationFirstLine.getId()).thenReturn(2L);
        when(addStationFirstLine.getId()).thenReturn(3L);
        when(upStationSecondLine.getId()).thenReturn(4L);
        when(downStationSecondLine.getId()).thenReturn(1L);
        when(upStationThirdLine.getId()).thenReturn(4L);
        when(downStationThirdLine.getId()).thenReturn(3L);

        Line firstLine = LineTestFixture.노선을_생성한다("1호선", "red", upStationFirstLine, downStationFirstLine, 10);
        firstLine.addStation(downStationFirstLine, addStationFirstLine, 5);

        Line secondLine = LineTestFixture.노선을_생성한다("2호선", "red", upStationSecondLine, downStationSecondLine, 10);
        Line thirdLine = LineTestFixture.노선을_생성한다("3호선", "red", upStationThirdLine, downStationThirdLine, 20);
        List<Line> lines = Lists.newArrayList(firstLine, secondLine, thirdLine);
        PathFinder pathFinder = new PathFinder(lines);

        assertThat(pathFinder.findPath(upStationSecondLine.getId(), downStationFirstLine.getId())).containsExactly(upStationSecondLine.getId(), upStationFirstLine.getId(), downStationFirstLine.getId());
    }

}