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
		이호선 = new Line("이호선", "초록색");
		성수뚝섬구간 = new Section(이호선, 성수역, 뚝섬역, new Distance(10));
	}

	@DisplayName("구간 생성 테스트")
	@Test
	void create() {
		Sections sections = new Sections();
		assertThat(sections).isNotNull();
	}

	@DisplayName("구간들의 역 순서 조회 기능 테스트")
	@Test
	void getStations() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		assertThat(sections.getStations()).containsAll(Arrays.asList(성수역, 뚝섬역));
	}

	@DisplayName("구간 추가 기능 테스트 - 맨 앞에 추가 하기")
	@Test
	void addSectionFirstSection() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 건대입구역, 성수역, new Distance(10)));
		assertThat(sections.getStations()).containsAll(Arrays.asList(건대입구역, 성수역, 뚝섬역));
	}

	@DisplayName("구간 추가 기능 테스트 - 중간에 추가 하기(앞을 기준으로)")
	@Test
	void addSectionBaseUpStation() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 성수역, 건대입구역, new Distance(5)));
		assertThat(sections.getStations()).containsAll(Arrays.asList(성수역, 건대입구역, 뚝섬역));
	}

	@DisplayName("구간 추가 기능 테스트 - 중간에 추가 하기(뒤를 기준으로)")
	@Test
	void addSectionBaseDownStation() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 건대입구역, 뚝섬역, new Distance(5)));
		assertThat(sections.getStations()).containsAll(Arrays.asList(성수역, 건대입구역, 뚝섬역));
	}

	@DisplayName("구간 추가 기능 테스트 - 마지막에 추가 하기")
	@Test
	void addSectionBaseLastSection() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 뚝섬역, 건대입구역, new Distance(10)));
		assertThat(sections.getStations()).containsAll(Arrays.asList(성수역, 뚝섬역, 건대입구역));
	}

	@DisplayName("구간 추가 기능 테스트 - 존재 하지 않는 상행역, 하행역 기준으로 추가하기 (에러 발생)")
	@Test
	void addSectionNotExistsStationInSections() {
		Sections sections = new Sections();
		Station 강변역 = new Station("강변역");
		sections.addSection(성수뚝섬구간);
		assertThatThrownBy(
			() -> sections.addSection(new Section(이호선, 강변역, 건대입구역, new Distance(10)))
		).isInstanceOf(RuntimeException.class); // todo : Exception Advise 추가
	}

	@DisplayName("구간 추가 기능 테스트 - 이미 존재하는 구간 똑같이 추가하기 (에러 발생)")
	@Test
	void addSectionAlreadyExistsStationInSections() {
		Sections sections = new Sections();
		Station 강변역 = new Station("강변역");
		sections.addSection(성수뚝섬구간);
		assertThatThrownBy(
			() -> sections.addSection(성수뚝섬구간)
		).isInstanceOf(RuntimeException.class); // todo : Exception Advise 추가
	}

	@DisplayName("구간 추가 기능 테스트 - 구간이 null 경우 (에러 발생)")
	@Test
	void addSectionNull() {
		Sections sections = new Sections();
		assertThatThrownBy(
			() -> sections.addSection(null)
		).isInstanceOf(RuntimeException.class); // todo : Exception Advise 추가
	}

	@DisplayName("구간에 역 제거 기능 테스트")
	@Test
	void removeStation() {
		Sections sections = new Sections();
		sections.addSection(성수뚝섬구간);
		sections.addSection(new Section(이호선, 건대입구역, 성수역, new Distance(10)));
		sections.removeStation(이호선, 성수역);
		assertThat(sections.getStations()).containsAll(Arrays.asList(건대입구역, 뚝섬역));
	}

	@DisplayName("구간에 역 제거 기능 테스트- 역이 null 경우 (에러 발생)")
	@Test
	void removeStationNull() {
		Sections sections = new Sections();
		assertThatThrownBy(
			() -> sections.removeStation(이호선, null)
		).isInstanceOf(RuntimeException.class); // todo : Exception Advise 추가
	}

	@DisplayName("구간에 역 제거 기능 테스트- 노선이 null 경우 (에러 발생)")
	@Test
	void removeLineNull() {
		Sections sections = new Sections();
		assertThatThrownBy(
			() -> sections.removeStation(null, 성수역)
		).isInstanceOf(RuntimeException.class); // todo : Exception Advise 추가
	}

}
