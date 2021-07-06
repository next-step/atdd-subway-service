package nextstep.subway.station;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationGraph;
import nextstep.subway.station.domain.StationPath;
import nextstep.subway.station.excpetion.StationGraphException;

public class StationGraphTest {

	Station 성수역;
	Station 뚝섬역;
	Station 건대입구역;
	Station 강남구청역;
	Station 왕십리역;
	Line 이호선;
	Line 칠호선;
	Line 수인분당선;
	Section 뚝섬성수구간;
	Section 성수건대구간;
	Section 건대강남구청역구간;
	Section 강남구청왕십리구간;
	Section 왕십리뚝섬구간;
	StationGraph 역그래프;

	/*
	왕십리 -4- 뚝섬 -2- 성수 -2- 건대
	  |						 |
	 10						 6
	  |						 |
	강남구청 ----------------강남구청
	 */

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		건대입구역 = new Station(3L, "건대입구역");
		강남구청역 = new Station(4L, "강남구청역");
		왕십리역 = new Station(5L, "왕십리역");

		이호선 = new Line("이호선", "초록색", new Fare(100));
		칠호선 = new Line("칠호선", "칠호선색", new Fare(200));
		수인분당선 = new Line("수인분당선", "노랑색", new Fare(300));

		왕십리뚝섬구간 = new Section(이호선, 왕십리역, 뚝섬역, new Distance(4));
		뚝섬성수구간 = new Section(이호선, 뚝섬역, 성수역, new Distance(2));
		성수건대구간 = new Section(이호선, 성수역, 건대입구역, new Distance(2));

		건대강남구청역구간 = new Section(칠호선, 건대입구역, 강남구청역, new Distance(6));
		강남구청왕십리구간 = new Section(수인분당선, 강남구청역, 왕십리역, new Distance(10));

		이호선.addLineStation(뚝섬성수구간);
		이호선.addLineStation(성수건대구간);
		이호선.addLineStation(왕십리뚝섬구간);
		칠호선.addLineStation(건대강남구청역구간);
		수인분당선.addLineStation(강남구청왕십리구간);
		역그래프 = new StationGraph(new Lines(Arrays.asList(이호선, 칠호선, 수인분당선)));
	}

	@DisplayName("StationGraph 생성")
	@Test
	void create() {
		assertThat(역그래프).isNotNull();
	}

	@DisplayName("StationGraph 최단거리 구하기")
	@Test
	void getShortestDistance() {
		StationPath path = 역그래프.getShortestPath(성수역, 강남구청역);
		assertThat(path.getDistance()).isEqualTo(8);
		assertThat(path.getStations()).containsAll(Arrays.asList(성수역, 건대입구역, 강남구청역));
	}

	@DisplayName("StationGraph 최단거리 구하기 - 출발역과 도착역이 같은 경우(에러 발생)")
	@Test
	void getShortestDistanceSourceEqualsTarget() {
		assertThatThrownBy(() -> 역그래프.getShortestPath(성수역, 성수역)).isInstanceOf(StationGraphException.class);
	}

	@DisplayName("StationGraph 최단거리 구하기 - 존재하지 않는 구간 조회(에러 발생)")
	@Test
	void getShortestDistanceNotExistStations() {
		Station 신도림역 = new Station("신도림역");
		Station 서울역 = new Station("서울역");
		assertThatThrownBy(() -> 역그래프.getShortestPath(신도림역, 서울역)).isInstanceOf(StationGraphException.class);
	}

	@DisplayName("StationGraph 최단거리 구하기 - 연결되지 않는 구간 조회(에러 발생)")
	@Test
	void getShortestDistanceNotConnected() {
		Station 신도림역 = new Station("신도림역");
		Station 서울역 = new Station("서울역");
		Line 일호선 = new Line("일호선", "파랑색", new Fare(100));
		Section 신도림서울역구간 = new Section(일호선, 신도림역, 서울역, new Distance(15));
		일호선.addLineStation(신도림서울역구간);
		역그래프 = new StationGraph(new Lines(Arrays.asList(일호선, 이호선, 칠호선, 수인분당선)));
		assertThatThrownBy(() -> 역그래프.getShortestPath(성수역, 서울역)).isInstanceOf(StationGraphException.class);
	}

}
