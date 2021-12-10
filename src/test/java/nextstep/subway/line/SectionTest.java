package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Section;
import nextstep.subway.station.StationTest;

public class SectionTest {

	public static final Section SECTION_1 = Section.of(1L, StationTest.노포역, StationTest.서면역, 10);
	public static final Section SECTION_2 = Section.of(2L, StationTest.부산진역, StationTest.다대포해수욕장역, 20);
	public static final Section SECTION_3 = Section.of(3L, StationTest.서면역, StationTest.범내골역, 20);

	@Test
	@DisplayName("생성한다")
	void create() {
		// given, when
		Section section = Section.of(1L, StationTest.노포역, StationTest.다대포해수욕장역, 40);

		// then
		assertThat(section).isEqualTo(SECTION_1);
	}
	
}
