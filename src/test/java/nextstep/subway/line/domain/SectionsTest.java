package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class SectionsTest {

	Station 성수역;
	Station 뚝섬역;
	Station 건대입구역;
	Line 이호선;
	Section 성수뚝섬구간;

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		건대입구역 = new Station(3L, "건대입구역");
		성수뚝섬구간 = new Section(이호선, 성수역, 뚝섬역, 10);
	}

	@DisplayName("구간들의 역 순서 조회 기능 테스트")
	@Test
	void getStations() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		assertThat(sections.getStations()).containsAll(Arrays.asList(성수역, 뚝섬역));
	}

	@DisplayName("구간 추가 기능 테스트")
	@Test
	void addSection() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 건대입구역, 성수역, 10));
		assertThat(sections.getStations()).containsAll(Arrays.asList(건대입구역, 성수역, 뚝섬역));
	}

	@DisplayName("구간에 역 제거 기능 테스트")
	@Test
	void removeStation() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 건대입구역, 성수역, 10));
		sections.removeStation(이호선, 성수역);
		assertThat(sections.getStations()).containsAll(Arrays.asList(건대입구역, 뚝섬역));
	}

}
