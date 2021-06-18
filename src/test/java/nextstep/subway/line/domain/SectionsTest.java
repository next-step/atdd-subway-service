package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

	@DisplayName("구간을 추가할 수 있다.")
	@Test
	void addTest() {
		Sections sections = new Sections();

		sections.add(강남_양재_구간);

		assertThat(sections.getSections()).containsExactly(강남_양재_구간);
	}
}