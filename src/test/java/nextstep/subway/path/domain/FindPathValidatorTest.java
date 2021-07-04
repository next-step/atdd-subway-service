package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FindPathValidatorTest {
	private FindPathValidator findPathValidator;

	private List<Station> stations;

	@BeforeEach
	void setUp() {
		findPathValidator = new FindPathValidator();
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		stations = Arrays.asList(강남역, 양재역);
	}

	@Test
	@DisplayName("출발역과 도착역이 같은 경우 익셉션 발생")
	void findPathValidationTest() {
		long sourceStationId = 1L;
		long targetStationId = 1L;
		assertThatThrownBy(() -> findPathValidator.validate(sourceStationId, targetStationId))
				.isInstanceOf(RuntimeException.class);
	}
}
