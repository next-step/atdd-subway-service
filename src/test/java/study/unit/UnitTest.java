package study.unit;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단위 테스트")
public class UnitTest {
	@Test
	void update() {
		// given
		Station upStation = new Station("강남역");
		Station downStation = new Station("광교역");
		Line line = new Line("신분당선", "RED", upStation, downStation, 10);

		// when
		line.update("신신분당선", "GREEN");

		// then
		assertThat(line.getName()).isEqualTo("신신분당선");
		assertThat(line.getColor()).isEqualTo("GREEN");
	}
}
