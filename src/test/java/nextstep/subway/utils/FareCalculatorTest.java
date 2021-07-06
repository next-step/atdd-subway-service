package nextstep.subway.utils;

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

public class FareCalculatorTest {

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

	@DisplayName("요금 계산 테스트 성인 10KM 미만")
	@Test
	void 요금_계산_테스트_성인_10KM_미만() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(5), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2250);
	}

	@DisplayName("요금 계산 테스트 성인 10KM 초과 50KM 이하")
	@Test
	void 요금_계산_테스트_성인_10KM_초과_50KM_이하() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(15), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2550);
	}

	@DisplayName("요금 계산 테스트 성인 50KM 초과")
	@Test
	void 요금_계산_테스트_성인_50KM_초과() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 20);
		assertThat(fare.value()).isEqualTo(2950);
	}

	@DisplayName("요금 계산 테스트 어린이 10KM 미만")
	@Test
	void 요금_계산_테스트_어린이_10KM_미만() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(8), new Fare(1000), 10);
		assertThat(fare.value()).isEqualTo(1300);
	}

	@DisplayName("요금 계산 테스트 어린이 10KM 초과 50KM 이하")
	@Test
	void 요금_계산_테스트_어린이_10KM_초과_50KM_이하() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(15), new Fare(1000), 10);
		assertThat(fare.value()).isEqualTo(1450);
	}

	@DisplayName("요금 계산 테스트 어린이 50KM 초과")
	@Test
	void 요금_계산_테스트_어린이_50KM_초과() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 10);
		assertThat(fare.value()).isEqualTo(1650);
	}

	@DisplayName("요금 계산 테스트 청소년 10KM 미만")
	@Test
	void 요금_계산_테스트_청소년_10KM_미만() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(8), new Fare(1000), 15);
		assertThat(fare.value()).isEqualTo(1870);
	}

	@DisplayName("요금 계산 테스트 청소년 10KM 초과 50KM 이하")
	@Test
	void 요금_계산_테스트_청소년_10KM_초과_50KM_이하() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(15), new Fare(1000), 15);
		assertThat(fare.value()).isEqualTo(2110);
	}

	@DisplayName("요금 계산 테스트 청소년 50KM 초과")
	@Test
	void 요금_계산_테스트_청소년_50KM_초과() {
		Fare fare = FareCalculator.getSubwayFare(new Distance(55), new Fare(1000), 15);
		assertThat(fare.value()).isEqualTo(2430);
	}

	@DisplayName("그래프를 통한 요금 계산 테스트")
	@Test
	void 그래프를_통한_요금_계산_테스트() {
		Lines lines = new Lines(Arrays.asList(이호선, 칠호선));
		StationPath 경로 = 역그래프.getShortestPath(성수역, 강남구청역);
		assertThat(FareCalculator.getSubwayFare(lines, 경로, 20).value()).isEqualTo(1450);
	}
}
