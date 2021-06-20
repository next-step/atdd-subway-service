package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SectionTest {
	@Test
	@DisplayName("sections내 section 추가")
	void when_section_not_exists_station_can_be_added() {
		Line line = new Line("2호선", "green");
		Sections sections = new Sections();
		sections.addLineStation(new Section(line, new Station("신촌역"), new Station("홍대입구역"), 10));

		assertThat(sections.getStations()).containsExactly(new Station("신촌역"), new Station("홍대입구역"));
	}

	@Test
	@DisplayName("공통의 up station 존재 시 section 추가")
	void when_common_up_station_exists_station_can_be_added() {
		Line line = new Line("2호선", "green");
		Sections sections = new Sections();
		sections.addLineStation(new Section(line, new Station("신촌역"), new Station("합정역"), 10));
		sections.addLineStation(new Section(line, new Station("신촌역"), new Station("홍대입구역"), 7));
		assertThat(sections.getStations()).containsExactly(new Station("신촌역"), new Station("홍대입구역"), new Station("합정역"));
	}

	@Test
	@DisplayName("공통의 down station 존재 시 section 추가")
	void when_common_down_station_exists_station_can_be_added() {
		Line line = new Line("2호선", "green");
		Sections sections = new Sections();
		sections.addLineStation(new Section(line, new Station("신촌역"), new Station("합정역"), 10));
		sections.addLineStation(new Section(line, new Station("홍대입구역"), new Station("합정역"), 7));
		assertThat(sections.getStations()).containsExactly(new Station("신촌역"), new Station("홍대입구역"), new Station("합정역"));
	}
}
