package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class PathFinderTest {
	List<Line> lines;

	@BeforeEach
	void setUp() {
		Station line2AndLine3TransferStation = new Station("교대역");
		Station line3Station = new Station("남부터미널역");
		Station line3AndNewBunDangTransferStation = new Station("양재역");
		Station lineNewBunDangAndLine2TransferStation = new Station("강남역");
		Line line2 = new Line("2호선", "green", line2AndLine3TransferStation, lineNewBunDangAndLine2TransferStation, 4, 1000);
		Line line3 = new Line("2호선", "orange", line2AndLine3TransferStation, line3AndNewBunDangTransferStation, 5, 700);
		line3.addLineStation(line3Station, line3AndNewBunDangTransferStation, 2);
		Line lineNewBunDang = new Line("신분당선", "pink", lineNewBunDangAndLine2TransferStation, line3AndNewBunDangTransferStation, 10);

		this.lines = Arrays.asList(line2, line3, lineNewBunDang);
	}

	@DisplayName("노선 추가 요금 계산")
	@Test
	void calculateLineAdditionalFareWithNoTransfer() {
		// given
		Station startStation = new Station("양재역");
		Station destinationStation = new Station("교대역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(1950);
	}

	@DisplayName("노선 추가 요금 계산")
	@Test
	void calculateLineAdditionalFareWithTransferTest() {
		// given
		Station startStation = new Station("남부터미널역");
		Station destinationStation = new Station("강남역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(2250);
	}

	@DisplayName("두 역만 사이 요금 계산(거리 170km)")
	@Test
	void calculateFareBetweenTwoStationTestOn170Kilometer() {
		// given
		Station startStation = new Station("신촌역");
		Station destinationStation = new Station("홍대입구역");
		Line line = new Line("2호선", "green", startStation, destinationStation, 170);
		PathFinder pathFinder = new PathFinder(Arrays.asList(line));

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(3550);
	}

	@DisplayName("두 역만 사이 요금 계산(거리 57km)")
	@Test
	void calculateFareBetweenTwoStationTestOn57Kilometer() {
		// given
		Station startStation = new Station("신촌역");
		Station destinationStation = new Station("홍대입구역");
		Line line = new Line("2호선", "green", startStation, destinationStation, 57);
		PathFinder pathFinder = new PathFinder(Arrays.asList(line));

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(2150);
	}

	@DisplayName("두 역만 사이 요금 계산(거리 39km)")
	@Test
	void calculateFareBetweenTwoStationTestOn39Kilometer() {
		// given
		Station startStation = new Station("신촌역");
		Station destinationStation = new Station("홍대입구역");
		Line line = new Line("2호선", "green", startStation, destinationStation, 39);
		PathFinder pathFinder = new PathFinder(Arrays.asList(line));

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(1850);
	}

	@DisplayName("두 역만 사이 요금 계산(거리 15km)")
	@Test
	void calculateFareBetweenTwoStationTestOn15Kilometer() {
		// given
		Station startStation = new Station("신촌역");
		Station destinationStation = new Station("홍대입구역");
		Line line = new Line("2호선", "green", startStation, destinationStation, 15);
		PathFinder pathFinder = new PathFinder(Arrays.asList(line));

		// when
		int fare = pathFinder.getFare(startStation, destinationStation);

		// then
		assertThat(fare).isEqualTo(1350);
	}

	@DisplayName("출발역과 도착역이 연결되어 있지 않을 경우 오류")
	@Test
	void findShortestPathWhenFromStartStationDestinationStationCanNotBeReached() {
		// given
		Station startStation = new Station("남부터미널역");
		Station destinationStation = new Station("신촌역");
		PathFinder pathFinder = new PathFinder(lines);

		// when, then
		assertThatThrownBy(() -> pathFinder.findPath(startStation, destinationStation)).isInstanceOf(RuntimeException.class);
	}

	@DisplayName("출발역과 도착역이 같은 경우 오류")
	@Test
	void findShortestPathWhenStartAndDestinationAreSame() {
		// given
		Station startStation = new Station("남부터미널역");
		Station destinationStation = new Station("남부터미널역");
		PathFinder pathFinder = new PathFinder(lines);

		// when, then
		assertThatThrownBy(() -> pathFinder.findPath(startStation, destinationStation)).isInstanceOf(RuntimeException.class);
	}

	@DisplayName("최단거리와 역방향 조회 시나리오")
	@Test
	void findReverseShortestPath() {
		// given
		Station startStation = new Station("교대역");
		Station destinationStation = new Station("양재역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		int length = pathFinder.findPathLength(startStation, destinationStation);
		int reverseLength = pathFinder.findPathLength(destinationStation, startStation);

		// then
		assertThat(length).isEqualTo(reverseLength);
	}

	@DisplayName("환승역에서 환승역으로 최단거리 조회")
	@Test
	void findShortestPathFromTransferToTransfer() {
		// given
		Station startStation = new Station("교대역");
		Station destinationStation = new Station("양재역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		List<String> shortestPath = pathFinder.findPath(startStation, destinationStation);

		// then
		List<String> expectedStationIds = Arrays.asList("교대역", "남부터미널역", "양재역");

		assertThat(shortestPath).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathFinder.findPathLength(startStation, destinationStation)).isEqualTo(5);
	}

	@DisplayName("환승역에서 미환승역으로 최단거리 조회")
	@Test
	void findShortestPathFromTransferToNotTransfer() {
		// given
		Station startStation = new Station("강남역");
		Station destinationStation = new Station("남부터미널역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		List<String> shortestPath = pathFinder.findPath(startStation, destinationStation);

		// then
		List<String> expectedStationIds = Arrays.asList("강남역", "교대역", "남부터미널역");

		assertThat(shortestPath).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathFinder.findPathLength(startStation, destinationStation)).isEqualTo(7);
	}

	@DisplayName("미환승역에서 환승역으로 최단거리 조회")
	@Test
	void findShortestPathFromNotTransferToTransfer() {
		// given
		Station startStation = new Station("남부터미널역");
		Station destinationStation = new Station("강남역");
		PathFinder pathFinder = new PathFinder(lines);

		// when
		List<String> shortestPath = pathFinder.findPath(startStation, destinationStation);

		// then
		List<String> expectedStationIds = Arrays.asList("남부터미널역", "교대역", "강남역");

		assertThat(shortestPath).containsExactlyElementsOf(expectedStationIds);
		assertThat(pathFinder.findPathLength(startStation, destinationStation)).isEqualTo(7);
	}

	@DisplayName("두 역만 존재할 때 최단거리 조회")
	@Test
	void findPathBetweenTwoStationTest() {
		// given
		Station startStation = new Station("신촌역");
		Station destinationStation = new Station("홍대입구역");
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
