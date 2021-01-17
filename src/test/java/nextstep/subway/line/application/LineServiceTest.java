package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;

@DisplayName("LineService 테스트")
@SpringBootTest
class LineServiceTest {

	@Autowired
	LineService lineService;

	@Autowired
	StationService stationService;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	private StationResponse 교대역_응답;
	private StationResponse 강남역_응답;
	private StationResponse 역삼역_응답;
	private StationResponse 선릉역_응답;
	private StationResponse 삼성역_응답;
	private StationResponse 잠실역_응답;
	private StationResponse 천호역_응답;
	private StationResponse 군자역_응답;
	private LineResponse 이호선_응답;
	private LineResponse 오호선_응답;

	@BeforeEach
	void setUp() {
		databaseCleanup.execute();
		교대역_응답 = stationService.saveStation(new StationRequest("교대역"));
		강남역_응답 = stationService.saveStation(new StationRequest("강남역"));
		역삼역_응답 = stationService.saveStation(new StationRequest("역삼역"));
		선릉역_응답 = stationService.saveStation(new StationRequest("선릉역"));
		삼성역_응답 = stationService.saveStation(new StationRequest("삼성역"));
		잠실역_응답 = stationService.saveStation(new StationRequest("잠실역"));
		천호역_응답 = stationService.saveStation(new StationRequest("천호역"));
		군자역_응답 = stationService.saveStation(new StationRequest("군자역"));

		이호선_응답 = lineService.saveLine(new LineRequest("2호선", "green", 강남역_응답.getId(), 삼성역_응답.getId(), 10));
		오호선_응답 = lineService.saveLine(new LineRequest("5호선", "purple", 천호역_응답.getId(), 군자역_응답.getId(), 15));
	}

	@Test
	void findAllLines() {
		List<LineResponse> lineResponses = lineService.findLines();
		assertThat(lineResponses)
			.map(LineResponse::getStations)
			.anySatisfy(it -> assertThat(it).map(StationResponse::getName).containsExactly("강남역", "삼성역"))
			.anySatisfy(it -> assertThat(it).map(StationResponse::getName).containsExactly("천호역", "군자역"));
	}

	@Test
	void findLineResponseById() {
		LineResponse lineResponse = lineService.findLineResponseById(이호선_응답.getId());
		assertThat(lineResponse.getStations()).map(StationResponse::getName).containsExactly("강남역", "삼성역");
	}

	@Test
	void deleteLineById() {
		LineResponse lineResponse = lineService.findLineResponseById(이호선_응답.getId());
		assertThat(lineResponse.getStations()).map(StationResponse::getName).containsExactly("강남역", "삼성역");
	}

	@DisplayName("addLineStation 테스트 (happy path)")
	@Test
	void addLineStation_shouldSuccess1() {
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 5));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("강남역", "역삼역", "삼성역");

		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(역삼역_응답.getId(), 선릉역_응답.getId(), 4));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("강남역", "역삼역", "선릉역", "삼성역");
	}

	@DisplayName("addLineStation 테스트 (happy path : 상행 확장)")
	@Test
	void addLineStation_shouldSuccess2() {
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(교대역_응답.getId(), 강남역_응답.getId(), 100));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("교대역", "강남역", "삼성역");

	}

	@DisplayName("addLineStation 테스트 (happy path : 하행 확장)")
	@Test
	void addLineStation_shouldSuccess3() {
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(삼성역_응답.getId(), 잠실역_응답.getId(), 100));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("강남역", "삼성역", "잠실역");
	}

	@DisplayName("addLineStation 테스트 (예외 케이스 : 기존 역 사이 길이와 같다)")
	@Test
	void addLineStation_shouldException1() {
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 4));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("강남역", "역삼역", "삼성역");

		assertThatThrownBy(() -> lineService.addLineStation(이호선_응답.getId(),
			new SectionRequest(역삼역_응답.getId(), 선릉역_응답.getId(), 6)))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("addLineStation 테스트 (예외 케이스 : 기존 역 사이 길이보다 크다)")
	@Test
	void addLineStation_shouldException2() {
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 4));

		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations())
			.map(StationResponse::getName)
			.containsExactly("강남역", "역삼역", "삼성역");

		assertThatThrownBy(() -> lineService.addLineStation(이호선_응답.getId(),
			new SectionRequest(역삼역_응답.getId(), 선릉역_응답.getId(), 7)))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("addLineStation 테스트 (예외 케이스 : 이미 노선에 모두 등록)")
	@Test
	void addLineStation_shouldException3() {
		assertThatThrownBy(() -> lineService.addLineStation(이호선_응답.getId(),
			new SectionRequest(강남역_응답.getId(), 삼성역_응답.getId(), 7)))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("addLineStation 테스트 (예외 케이스 : 상행역과 하행역 둘 중 하나도 포함되어있지 않음)")
	@Test
	void addLineStation_shouldException4() {
		assertThatThrownBy(() -> lineService.addLineStation(이호선_응답.getId(),
			new SectionRequest(교대역_응답.getId(), 잠실역_응답.getId(), 7)))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("addLineStation 테스트 (예외 케이스 : 상행역과 하행역이 같음)")
	@Test
	void addLineStation_shouldException5() {
		assertThatThrownBy(() -> lineService.addLineStation(이호선_응답.getId(),
			new SectionRequest(강남역_응답.getId(), 강남역_응답.getId(), 7)))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("removeLineStation 테스트 (happy path : 종점 미포함)")
	@Test
	void removeLineStation_happyPath1() {
		// 강남역 - 역삼역 - 선릉역 - 삼성역
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 4));
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(역삼역_응답.getId(), 선릉역_응답.getId(), 5));

		lineService.removeLineStation(이호선_응답.getId(), 역삼역_응답.getId());

		// 강남역 - 선릉역 - 삼성역
		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations()).map(StationResponse::getName)
			.containsExactly("강남역", "선릉역", "삼성역");

		lineService.removeLineStation(이호선_응답.getId(), 선릉역_응답.getId());

		// 강남역 - 삼성역
		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations()).map(StationResponse::getName)
			.containsExactly("강남역", "삼성역");
	}

	@DisplayName("removeLineStation 테스트 (happy path : 상행 종점 포함)")
	@Test
	void removeLineStation_happyPath2() {
		// 강남역 - 역삼역 - 삼성역
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 4));

		lineService.removeLineStation(이호선_응답.getId(), 강남역_응답.getId());

		// 역삼역 - 삼성역
		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations()).map(StationResponse::getName)
			.containsExactly("역삼역", "삼성역");
	}

	@DisplayName("removeLineStation 테스트 (happy path : 하행 종점 포함)")
	@Test
	void removeLineStation_happyPath3() {
		// 강남역 - 역삼역 - 삼성역
		lineService.addLineStation(이호선_응답.getId(), new SectionRequest(강남역_응답.getId(), 역삼역_응답.getId(), 4));

		lineService.removeLineStation(이호선_응답.getId(), 삼성역_응답.getId());

		// 강남역 - 역삼역
		assertThat(lineService.findLineResponseById(이호선_응답.getId()).getStations()).map(StationResponse::getName)
			.containsExactly("강남역", "역삼역");
	}

	@DisplayName("removeLineStation 테스트 (예외 케이스 : 마지막 구간 제거)")
	@Test
	void removeLineStation_exceptionCase1() {
		assertThatThrownBy(() -> lineService.removeLineStation(이호선_응답.getId(), 삼성역_응답.getId()))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("removeLineStation 테스트 (예외 케이스 : 미포함 역 제거)")
	@Test
	void removeLineStation_exceptionCase2() {
		assertThatThrownBy(() -> lineService.removeLineStation(이호선_응답.getId(), 잠실역_응답.getId()))
			.isInstanceOf(RuntimeException.class);
	}
}
