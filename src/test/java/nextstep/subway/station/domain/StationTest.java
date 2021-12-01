package nextstep.subway.station.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 역")
class StationTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given

		// when
		Station station = Station.of("강남역");

		// then
		assertThat(station).isNotNull();
	}
}
