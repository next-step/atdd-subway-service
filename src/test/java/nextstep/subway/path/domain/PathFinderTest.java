package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
	@Test
	void findPathBetweenTwoStationTest() {
		// given
		Station startStation = new Station(1L, "신촌역");
		Station destinationStation = new Station(2L, "홍대입구역");
		Line line = new Line("2호선", "green", startStation, destinationStation, 7);
		PathFinder pathFinder = new PathFinder(Arrays.asList(line));

		// when
		List<String> shortestPath = pathFinder.findPath(startStation, destinationStation);

		// then
		List<String> expectedStationIds = Arrays.asList("신촌역", "홍대입구역");

		assertThat(shortestPath).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathFinder.findPathLength(startStation, destinationStation)).isEqualTo(7);
	}
}
