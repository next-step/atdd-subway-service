package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.subway.fare.domain.StandardFare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponseDto;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class PathsTest {

	private Map<String, Station> stationMap;
	private Map<String, Line> lineMap;
	private Map<String, Section> sectionMap;

	@BeforeEach
	void setUp() {
		지하철_역_등록();
		지하철_노선_구선_등록();
	}

	@Test
	void create() {
		//given
		List<Section> sections = new ArrayList<>(sectionMap.values());

		//when
		Paths paths = new Paths(sections);

		//then
		assertThat(paths).isNotNull();
	}

	@DisplayName("최단경로 구하기")
	@Test
	void getPath() {
		//given
		List<Section> sections = new ArrayList<>(sectionMap.values());
		Paths paths = new Paths(sections);

		//when
		PathResponseDto path = paths.getPath(stationMap.get("교대역"), stationMap.get("양재역"), new StandardFare());

		//then
		assertThat(path.getStations()).containsExactly(
			  StationResponse.of(stationMap.get("교대역")),
			  StationResponse.of(stationMap.get("남부터미널역")),
			  StationResponse.of(stationMap.get("양재역")));
		assertThat(path.getDistance()).isEqualTo(8);
	}

	@DisplayName("구간에 등록되지 않은 역의 경로는 구할 수 없다.")
	@Test
	void getPathNotIncludeSection() {
		//given
		List<Section> sections = new ArrayList<>(sectionMap.values());
		Paths paths = new Paths(sections);

		//when
		assertThatThrownBy(() -> paths.getPath(stationMap.get("교대역"), stationMap.get("고매역"), new StandardFare()))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("구간정보에 등록된 출발역(도착역)이 없습니다.");
	}

	@DisplayName("출발역과 도착역이 같은경우 경로를 구할 수 없다.")
	@Test
	void getPathSameSourceAndTarget() {
		//given
		List<Section> sections = new ArrayList<>(sectionMap.values());
		Paths paths = new Paths(sections);

		//when
		assertThatThrownBy(() -> paths.getPath(stationMap.get("교대역"), stationMap.get("교대역"), new StandardFare()))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("출발역과 도착역이 같습니다.");
	}

	@DisplayName("구간이 연결되지 않은 경우 경로를 구할 수 없다.")
	@Test
	void getPathNotLinkedTarget() {
		//given
		List<Section> sections = new ArrayList<>(sectionMap.values());
		Paths paths = new Paths(sections);

		//when
		assertThatThrownBy(() -> paths.getPath(stationMap.get("교대역"), stationMap.get("삼성역"), new StandardFare()))
			  .isInstanceOf(RuntimeException.class)
			  .hasMessage("경로가 존재하지 않습니다.");
	}

	private void 지하철_역_등록() {
		this.stationMap = new HashMap<>();
		Station 강남역 = new Station("강남역");
		Station 양재역 = new Station("양재역");
		Station 교대역 = new Station("교대역");
		Station 남부터미널역 = new Station("남부터미널역");
		Station 고매역 = new Station("고매역");
		Station 선릉역 = new Station("선릉역");
		Station 삼성역 = new Station("삼성역");
		ReflectionTestUtils.setField(강남역, "id", 1L);
		ReflectionTestUtils.setField(양재역, "id", 2L);
		ReflectionTestUtils.setField(교대역, "id", 3L);
		ReflectionTestUtils.setField(남부터미널역, "id", 4L);
		ReflectionTestUtils.setField(고매역, "id", 5L);
		ReflectionTestUtils.setField(선릉역, "id", 6L);
		ReflectionTestUtils.setField(삼성역, "id", 7L);

		stationMap.put("강남역", 강남역);
		stationMap.put("양재역", 양재역);
		stationMap.put("교대역", 교대역);
		stationMap.put("남부터미널역", 남부터미널역);
		stationMap.put("고매역", 고매역);
		stationMap.put("선릉역", 선릉역);
		stationMap.put("삼성역", 삼성역);
	}

	private void 지하철_노선_구선_등록() {
		this.lineMap = new HashMap<>();
		Line 신분당선 = new Line("신분당선", "red", stationMap.get("강남역"), stationMap.get("양재역"), 10);
		Line 이호선 = new Line("이호선", "green", stationMap.get("교대역"), stationMap.get("강남역"), 10);
		Line 삼호선 = new Line("삼호선", "orange", stationMap.get("남부터미널역"), stationMap.get("양재역"), 5);
		ReflectionTestUtils.setField(신분당선, "id", 10L);
		ReflectionTestUtils.setField(이호선, "id", 11L);
		ReflectionTestUtils.setField(삼호선, "id", 12L);

		lineMap.put("신분당선", 신분당선);
		lineMap.put("이호선", 이호선);
		lineMap.put("삼호선", 삼호선);
		이호선.addSection(stationMap.get("선릉역"), stationMap.get("삼성역"), 3);
		삼호선.addSection(stationMap.get("교대역"), stationMap.get("남부터미널역"), 3);

		this.sectionMap = new HashMap<>();
		Section 신분당선_구간 = 신분당선.getSections().get(0);
		Section 이호선_구간 = 이호선.getSections().get(0);
		Section 이호선_구간1 = 이호선.getSections().get(1);
		Section 삼호선_구간1 = 삼호선.getSections().get(0);
		Section 삼호선_구간2 = 삼호선.getSections().get(1);
		ReflectionTestUtils.setField(신분당선_구간, "id", 20L);
		ReflectionTestUtils.setField(이호선_구간, "id", 21L);
		ReflectionTestUtils.setField(삼호선_구간1, "id", 22L);
		ReflectionTestUtils.setField(삼호선_구간2, "id", 23L);
		ReflectionTestUtils.setField(이호선_구간1, "id", 24L);

		sectionMap.put("강남-신분당선_구간", 신분당선_구간);
		sectionMap.put("이호선_구간", 이호선_구간);
		sectionMap.put("이호선_구간1", 이호선_구간1);
		sectionMap.put("삼호선_구간1", 삼호선_구간1);
		sectionMap.put("삼호선_구간2", 삼호선_구간2);
	}
}
