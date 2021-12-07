package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

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
		firstSection = new Section(1L, null, new Station("두정역"), new Station("천안역"), 15);
		secondSection = new Section(2L, null, new Station("천안역"), new Station("봉명역"), 10);
		thirdSection = new Section(3L, null, new Station("봉명역"), new Station("쌍용역"), 20);
		sections = new Sections(thirdSection, secondSection, firstSection);
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
			, new Station("봉명역"), new Station("쌍용역"));
	}

	@Test
	@DisplayName("중간에 구간 추가(시작점 일치) 테스트")
	public void addSectionMatchStartLocationTest() {
		//given
		// when
		sections.addSection(new Section(null, new Station("봉명역"), new Station("아산역"), 10));
		//then
		assertThat(sections.getStations()).containsExactly(new Station("두정역"), new Station("천안역")
			, new Station("봉명역"), new Station("아산역"), new Station("쌍용역"));
	}

	@Test
	@DisplayName("중간에 구간 추가(종점 일치) 테스트")
	public void addSectionMatchEndLocationTest() {
		//given
		// when
		sections.addSection(new Section(null, new Station("아산역"), new Station("쌍용역"), 10));
		//then
		assertThat(sections.getStations()).containsExactly(new Station("두정역"), new Station("천안역")
			, new Station("봉명역"), new Station("아산역"), new Station("쌍용역"));
	}

	@Test
	@DisplayName("시작지점에 구간 추가 테스트")
	public void addSectionStartLocationTest() {
		//given
		// when
		sections.addSection(new Section(null, new Station("평택역"), new Station("두정역"), 10));
		//then
		assertThat(sections.getStations()).containsExactly(new Station("평택역"), new Station("두정역"), new Station("천안역")
			, new Station("봉명역"), new Station("쌍용역"));
	}

	@Test
	@DisplayName("종점에 구간 추가 테스트")
	public void addSectionEndLocationTest() {
		//given
		// when
		sections.addSection(new Section(null, new Station("쌍용역"), new Station("아산역"), 10));
		//then
		assertThat(sections.getStations()).containsExactly(new Station("두정역"), new Station("천안역")
			, new Station("봉명역"), new Station("쌍용역"), new Station("아산역"));
	}

	@Test
	@DisplayName("전부 존재하는 역 구간 추가 실패")
	public void addSectionExistedTest() {
		assertThatThrownBy(() -> sections.addSection(new Section(null, new Station("두정역"), new Station("천안역"), 10)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("미 존재하는 역 구간 추가 실패")
	public void addSectionNonMatchTest() {
		assertThatThrownBy(() -> sections.addSection(new Section(null, new Station("강남"), new Station("판교"), 10)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("추가구간 길이가 더 긴경우 추가 실패")
	public void addSectionLengthTest() {
		assertThatThrownBy(() -> sections.addSection(new Section(null, new Station("봉명"), new Station("아산"), 100)))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("노선 내 지하철 역 삭제")
	public void removeStationTest() {
		//given
		// when
		sections.remove(null, new Station("천안역"));
		//then
		assertThat(sections.getStations()).containsExactly(new Station("두정역"), new Station("봉명역"), new Station("쌍용역"));
	}

	@Test
	@DisplayName("노선 내 구간 1개만 있을 때 삭제 실패")
	public void removeSectionsSizeOneTest() {
		//given
		// when
		Sections sections = new Sections(firstSection);
		//then
		assertThatThrownBy(() -> sections.remove(null, new Station("천안역")))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
