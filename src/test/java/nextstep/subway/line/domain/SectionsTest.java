package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.SortedStations;
import nextstep.subway.station.domain.Station;

class SectionsTest {

	private Sections sections;
	private Station 강남역;
	private Station 서초역;
	private Station 교대역;
	private Station 역삼역;
	private Line line;

	@BeforeEach
	void setUp() {
		this.sections = new Sections();
		this.강남역 = new Station("강남역");
		this.서초역 = new Station("서초역");
		this.교대역 = new Station("교대역");
		this.역삼역 = new Station("역삼역");
		this.line = new Line("2호선", "green");

		this.sections.addSection(new Section(this.line, this.교대역, this.서초역, 10));
		this.sections.addSection(new Section(this.line, this.강남역, this.교대역, 10));
		this.sections.addSection(new Section(this.line, this.역삼역, this.강남역, 10));
	}

	@DisplayName("상행선에서 하행선 순으로 역을 정렬하여 반환하는지 테스트")
	@Test
	void testGetStations() {
		SortedStations stations = this.sections.getSortedStations();
		assertThat(stations.getStations()).containsExactly(역삼역, 강남역, 교대역, 서초역);
	}

	@DisplayName("이미 존재하는 구간에 새로운 구간을 추가하려는 경우 오류발생")
	@Test
	void testValidateAlreadyExist() {
		assertThatThrownBy(() -> {
			this.sections.addSection(new Section(this.line, this.강남역, this.서초역, 10));
		}).isInstanceOf(RuntimeException.class)
			.hasMessageContaining("이미 등록된 구간 입니다.");
	}

	@DisplayName("구간추가 테스트 - 상, 하행 종점 및 중간 구간 추가")
	@Test
	void testAddSection() {
		Station 잠실역 = new Station("잠실역");
		this.sections.addSection(new Section(this.line, 잠실역, this.역삼역, 10));
		assertThat(this.sections.getSortedStations().getStations()).containsExactly(잠실역, 역삼역, 강남역, 교대역, 서초역);

		Station 사당역 = new Station("사당역");
		this.sections.addSection(new Section(this.line, this.서초역, 사당역, 10));
		assertThat(this.sections.getSortedStations().getStations())
			.containsExactly(잠실역, 역삼역, 강남역, 교대역, 서초역, 사당역);

		Station 삼성역 = new Station("삼성역");
		this.sections.addSection(new Section(this.line, 삼성역, this.역삼역, 5));
		assertThat(this.sections.getSortedStations().getStations())
			.containsExactly(잠실역, 삼성역, 역삼역, 강남역, 교대역, 서초역, 사당역);
	}

	@DisplayName("관련없는 구간을 추가시 오류발생")
	@Test
	void testNotContainedStations() {
		Station 없는역1 = new Station("test1");
		Station 없는역2 = new Station("test2");
		assertThatThrownBy(() -> {
			this.sections.addSection(new Section(this.line, 없는역1, 없는역2, 10));
		}).isInstanceOf(RuntimeException.class)
			.hasMessageContaining("등록할 수 없는 구간 입니다.");
	}

	@DisplayName("구간 제거를 테스트")
	@Test
	void testRemoveSection() {
		this.sections.removeSection(강남역);
		assertThat(this.sections.getSortedStations().getStations()).containsExactly(역삼역, 교대역, 서초역);
	}

	@DisplayName("구간이 하나일때 제거시 오류")
	@Test
	void testRemoveError() {
		this.sections.removeSection(강남역);
		this.sections.removeSection(교대역);

		assertThatThrownBy(() -> {
			this.sections.removeSection(서초역);
		}).isInstanceOf(RuntimeException.class);
	}

}
