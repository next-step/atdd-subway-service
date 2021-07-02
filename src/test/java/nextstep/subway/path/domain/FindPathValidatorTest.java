package nextstep.subway.path.domain;

import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FindPathValidatorTest {
	private FindPathValidator findPathValidator;

	private List<StationResponse> stations;

	@BeforeEach
	void setUp() {
		findPathValidator = new FindPathValidator();
		StationResponse 강남역 = new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
		StationResponse 양재역 = new StationResponse(2L, "양재역", LocalDateTime.now(), LocalDateTime.now());
		stations = Arrays.asList(강남역, 양재역);
	}

	@Test
	@DisplayName("출발역과 도착역이 같은 경우 익셉션 발생")
	void findPathValidationTest() {
		Long sourceStationId = 1L;
		Long targetStationId = 1L;
		assertThatThrownBy(() -> findPathValidator.validate(sourceStationId, targetStationId, stations))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("존재하지 않는 출발역이나 도착역을 조회 할 경우 익셉션 발생")
	void findPathValidationTest2() {
		Long sourceStationId = 1L;
		Long targetStationId = 3L;
		assertThatThrownBy(() -> findPathValidator.validate(sourceStationId, targetStationId, stations))
				.isInstanceOf(RuntimeException.class);
	}
}
