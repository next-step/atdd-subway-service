package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.StationTest;

public class SectionTest {
	@Test
	@DisplayName("상행역, 하행역 중에 지하철역이 있는지 여부를 반환한다.")
	void contains_success() {
		Line line = new Line();
		Section section = new Section(line, StationTest.삼성역, StationTest.선릉역, 5);

		assertThat(section.contains(StationTest.삼성역)).isTrue();
		assertThat(section.contains(StationTest.선릉역)).isTrue();
		assertThat(section.contains(StationTest.강남역)).isFalse();
	}
}
