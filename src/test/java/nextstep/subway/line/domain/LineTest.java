package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {
	private Station 강남역;
	private Station 판교역;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		판교역 = new Station("판교역");
	}

	@DisplayName("노선의 지하철역 목록을 조회한다.")
	@Test
	void getStations() {
		Line line = new Line("신분당선", "bg-red-600", 강남역, 판교역, 10);
		assertThat(line.getStations())
			.hasSize(2)
			.containsExactly(강남역, 판교역);
	}
}