package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

class FareTest {

	private Map<String, Station> stationMap;
	private Map<String, Line> lineMap;

	@BeforeEach
	void setUp() {
		지하철_역_등록();
		지하철_노선_등록();
	}

	@DisplayName("거리에 따른 구간요금을 적용한다.")
	@ParameterizedTest
	@CsvSource(value = {"10,1250", "11,1350", "51,2150"})
	void calculateFare(int distance, int expectedFare) {
		//givne
		List<Section> sections = Arrays.asList(
			  new Section(lineMap.get("이호선"), stationMap.get("강남역"), stationMap.get("교대역"),
					distance));
		List<Station> path = Arrays.asList(stationMap.get("강남역"), stationMap.get("교대역"));
		Fare fare = new StandardFare();

		//when
		fare.calculateFare(path, sections, distance);
		//then
		assertThat(fare.getFare()).isEqualTo(expectedFare);
	}

	@DisplayName("노선의 추가요금은 가장비싼 요금을 적용한다.")
	@Test
	void getPathApplyMaxAdditionalFare() {
		//givne
		int maxAdditionalFare = 200;
		Line line2 = new Line("2호선", "green", 100);
		Line line3 = new Line("3호선", "orange", maxAdditionalFare);

		List<Section> sections = Arrays.asList(
			  new Section(line2, stationMap.get("강남역"), stationMap.get("교대역"), 9),
			  new Section(line3, stationMap.get("교대역"), stationMap.get("남부터미널역"), 1)
		);
		List<Station> path = Arrays
			  .asList(stationMap.get("강남역"), stationMap.get("교대역"), stationMap.get("남부터미널역"));
		Fare fare = new StandardFare();

		//when
		fare.calculateFare(path, sections, 10);

		//then
		assertThat(fare.getFare()).isEqualTo(1_250 + maxAdditionalFare);
	}

	@DisplayName("청소년 할인 적용한 요금 계산")
	@Test
	void calculateTeenFare() {
		// (totalFare - 350) * 0.8
		int distance = 10;

		//givne
		List<Section> sections = Arrays.asList(
			  new Section(lineMap.get("이호선"), stationMap.get("강남역"), stationMap.get("교대역"),
					distance));
		List<Station> path = Arrays.asList(stationMap.get("강남역"), stationMap.get("교대역"));
		Fare fare = new TeenagerFare();

		//when
		fare.calculateFare(path, sections, distance);
		//then
		assertThat(fare.getFare()).isEqualTo(720);
	}

	@DisplayName("어린이 할인 적용한 요금 계산")
	@Test
	void calculateChildFare() {
		// (totalFare - 350) * 0.8
		int distance = 10;

		//givne
		List<Section> sections = Arrays.asList(
			  new Section(lineMap.get("이호선"), stationMap.get("강남역"), stationMap.get("교대역"),
					distance));
		List<Station> path = Arrays.asList(stationMap.get("강남역"), stationMap.get("교대역"));
		Fare fare = new ChildFare();

		//when
		fare.calculateFare(path, sections, distance);
		//then
		assertThat(fare.getFare()).isEqualTo(450);
	}

	private void 지하철_역_등록() {
		this.stationMap = new HashMap<>();
		Station 강남역 = new Station("강남역");
		Station 교대역 = new Station("교대역");
		Station 남부터미널역 = new Station("남부터미널역");
		ReflectionTestUtils.setField(강남역, "id", 1L);
		ReflectionTestUtils.setField(교대역, "id", 3L);
		ReflectionTestUtils.setField(남부터미널역, "id", 4L);

		stationMap.put("강남역", 강남역);
		stationMap.put("교대역", 교대역);
		stationMap.put("남부터미널역", 남부터미널역);
	}

	private void 지하철_노선_등록() {
		this.lineMap = new HashMap<>();
		Line 이호선 = new Line("이호선", "green");
		Line 삼호선 = new Line("삼호선", "orange");
		ReflectionTestUtils.setField(이호선, "id", 11L);
		ReflectionTestUtils.setField(삼호선, "id", 12L);

		lineMap.put("이호선", 이호선);
		lineMap.put("삼호선", 삼호선);
	}
}
