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

	@DisplayName("생성한다")
	@Test
	void create() {
		// given, when
		Section section = Section.of(1L, StationTest.노포역, StationTest.다대포해수욕장역, 40);

		// then
		assertThat(section).isEqualTo(SECTION_1);
	}

	@DisplayName("상행역을 업데이트한다")
	@Test
	void updateUpStationTest() {
		// given
		Section section = Section.of(1L, StationTest.노포역, StationTest.다대포해수욕장역, 10);

		// when
		section.updateUpStation(StationTest.서면역, 1);

		// then
		assertThat(section.getUpStation()).isEqualTo(StationTest.서면역);
		assertThat(section.getDistance()).isEqualTo(9);
	}

	@DisplayName("하행역을 업데이트한다")
	@Test
	void updateDownStationTest() {
		// given
		Section section = Section.of(1L, StationTest.노포역, StationTest.다대포해수욕장역, 10);

		// when
		section.updateDownStation(StationTest.서면역, 1);

		// then
		assertThat(section.getDownStation()).isEqualTo(StationTest.서면역);
		assertThat(section.getDistance()).isEqualTo(9);
	}

}
