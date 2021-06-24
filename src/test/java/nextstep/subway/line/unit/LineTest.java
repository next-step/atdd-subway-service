package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

@DisplayName("Line 단위테스트")
public class LineTest {

	@DisplayName("역 목록 반환")
	@Test
	public void getStations() {
		Station 운정역 = new Station("운정역");
		Station 일산역 = new Station("일산역");
		Line line = new Line("경의선", "BLUE",운정역, 일산역, 15);

		List<Station> stations = line.getStations();
		assertThat(stations).containsExactly(운정역, 일산역);
	}

}
