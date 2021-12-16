package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SectionsTest {
	@Test
	@DisplayName("구간을 추가한다.")
	void add_success() {
		Line line = new Line();
		Sections sections = new Sections(new ArrayList<>());
		Section section = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);

		sections.add(section);

		assertThat(new Sections(Collections.singletonList(section))).isEqualTo(sections);
	}

	@Test
	@DisplayName("구간을 삭제한다.")
	void remove_success() {
		Line line = new Line();
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section1);
		sections.add(section2);

		sections.remove(section1);

		assertThat(new Sections(Collections.singletonList(section2))).isEqualTo(sections);
	}

	@Test
	@DisplayName("상행구간부터 하행구간 순서대로 지하철역 반환")
	void getOrderedStations_success() {
		Line line = new Line();
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section2);
		sections.add(section1);

		List<Station> stations = sections.getOrderedStations();

		assertThat(Arrays.asList(LineTest.삼성역, LineTest.선릉역, LineTest.역삼역)).isEqualTo(stations);
	}
}
