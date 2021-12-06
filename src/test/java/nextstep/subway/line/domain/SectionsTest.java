package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	private Section firstSection;
	private Section secondSection;
	private Section thirdSection;
	private Sections sections;

	@BeforeEach
	public void setUp() {
		firstSection = new Section(null, new Station("두정역"), new Station("천안역"), 15);
		secondSection = new Section(null, new Station("천안역"), new Station("봉명역"), 10);
		thirdSection = new Section(null, new Station("봉명역"), new Station("쌍용역"), 5);
		sections = new Sections();
		sections.add(thirdSection, secondSection, firstSection);
	}

	@Test
	@DisplayName("구간 생성 테스트")
	public void createSectionsTest() {
		assertThat(sections).isNotNull();
	}

	@Test
	@DisplayName("구간 내 전체 지하철역 조회 테스트")
	public void findAllStationInSectionsTest() {
		//given
		// when
		List<Station> stations = sections.getStations();
		//then
		assertThat(stations).containsExactly(new Station("두정역"), new Station("천안역")
			,new Station("봉명역"), new Station("쌍용역"));
	}

}
