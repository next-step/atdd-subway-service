package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

@DisplayName("Section 단위테스트")
public class SectionsTest {

	@DisplayName("add 검증")
	@Test
	void add() {
		Sections sections = new Sections();
		sections.addSection(new Section());
		assertThat(sections.isEmpty()).isFalse();
	}

	@DisplayName("isEmpty 검증")
	@Test
	void isEmpty() {
		Sections sections = new Sections();
		assertThat(sections.isEmpty()).isTrue();
	}

	@DisplayName("findFirstStation 검증")
	@Test
	void findFirstStation() {
		Station 운정역 = new Station("운정역");
		Station 야당역 = new Station("야당역");
		Station 탄현역 = new Station("탄현역");
		Station 일산역 = new Station("일산역");

		Sections sections = new Sections(Arrays.asList(new Section(null, 운정역, 야당역, 1), new Section(null, 야당역, 탄현역, 2), new Section(null, 탄현역, 일산역, 3)));
		Station first = sections.findFirstStation();

		assertThat(first).isEqualTo(운정역);
	}

	@DisplayName("getStations 검증")
	@Test
	void getStations() {
		Station 운정역 = new Station("운정역");
		Station 야당역 = new Station("야당역");
		Station 탄현역 = new Station("탄현역");
		Station 일산역 = new Station("일산역");

		Sections sections = new Sections(Arrays.asList(new Section(null, 운정역, 야당역, 1), new Section(null, 야당역, 탄현역, 2), new Section(null, 탄현역, 일산역, 3)));

		assertThat(sections.getStations()).containsExactly(운정역, 야당역, 탄현역, 일산역);
	}
}
