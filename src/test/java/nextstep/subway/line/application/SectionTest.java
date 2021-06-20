package nextstep.subway.line.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SectionTest {
	Line line;
	Sections sections;
	Station sinChonStation;
	Station hongDaeStation;
	Station hapJeungStation;

	@BeforeEach
	void setUp() {
		line = new Line("2호선", "green");
		sections = new Sections();
		sinChonStation = new Station("신촌역");
		hongDaeStation = new Station("홍대입구역");
		hapJeungStation = new Station("합정역");
	}

	@Test
	@DisplayName("sections 내 section 추가")
	void when_section_not_exists_station_can_be_added() {
		sections.addLineStation(new Section(line, sinChonStation, hongDaeStation, 10));

		assertThat(sections.getStations()).containsExactly(sinChonStation, hongDaeStation);
	}

	@Test
	@DisplayName("공통의 up station 존재 시 section 추가")
	void when_common_up_station_exists_station_can_be_added() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));
		sections.addLineStation(new Section(line, sinChonStation, hongDaeStation, 7));

		assertThat(sections.getStations()).containsExactly(sinChonStation, hongDaeStation, hapJeungStation);
	}

	@Test
	@DisplayName("공통의 down station 존재 시 section 추가")
	void when_common_down_station_exists_station_can_be_added() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));
		sections.addLineStation(new Section(line, hongDaeStation, hapJeungStation, 7));

		assertThat(sections.getStations()).containsExactly(sinChonStation, hongDaeStation, hapJeungStation);
	}

	@Test
	@DisplayName("section 크기가 1일 땐 station을 삭제할 수 없음")
	void when_sections_size_one_station_can_not_be_removed() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));

		assertThatThrownBy(() -> sections.removeLineStation(line, sinChonStation)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("가운데 station 삭제하면 해당 station과 관계된 역이 연결")
	void when_station_removed_station_connected_to_the_station_connected() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));
		sections.addLineStation(new Section(line, hongDaeStation, hapJeungStation, 7));
		sections.removeLineStation(line, hongDaeStation);

		assertThat(sections.getStations()).containsExactly(sinChonStation, hapJeungStation);
	}

	@Test
	@DisplayName("상행 station 삭제하면 해당 station과 관계된 역이 연결")
	void when_up_station_removed_station_connected_to_the_station_connected() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));
		sections.addLineStation(new Section(line, hongDaeStation, hapJeungStation, 7));
		sections.removeLineStation(line, sinChonStation);

		assertThat(sections.getStations()).containsExactly(hongDaeStation, hapJeungStation);
	}

	@Test
	@DisplayName("하행 station 삭제하면 해당 station과 관계된 역이 연결")
	void when_down_station_removed_station_connected_to_the_station_connected() {
		sections.addLineStation(new Section(line, sinChonStation, hapJeungStation, 10));
		sections.addLineStation(new Section(line, hongDaeStation, hapJeungStation, 7));
		sections.removeLineStation(line, hapJeungStation);

		assertThat(sections.getStations()).containsExactly(sinChonStation, hongDaeStation);
	}
}
