package study.unit;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@DisplayName("단위 테스트")
public class UnitTest {
	@Test
	void update() {
		// given
		String newName = "구분당선";

		Station upStation = new Station("강남역");
		Station downStation = new Station("광교역");
		Line line = new Line("신분당선", "RED", new Fare(100));
		line.addLineStation(new Section(line, upStation, downStation, new Distance(10)));
		Line newLine = new Line(newName, "GREEN", new Fare(200));

		// when
		line.update(newLine);

		// then
		assertThat(line.getName()).isEqualTo(newName);
	}
}
