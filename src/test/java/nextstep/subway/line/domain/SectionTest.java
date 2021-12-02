package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간")
class SectionTest {
	@DisplayName("생성")
	@Test
	void of() {
		// given

		// when
		Section section = Section.of(강남역(), 광교역(), 20);

		// then
		assertThat(section).isNotNull();
	}

	@DisplayName("노선을 설정한다.")
	@Test
	void setLine() {
		// given
		Line line = 신분당선_강남역_광교역();
		Section section = 강남역_양재역_구간();

		// when
		section.setLine(line);

		// then
		assertThat(section.getLine()).isEqualTo(line);
	}
}
