package nextstep.subway.line.domain;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
		List<Station> actual = this.sections.getStations();
		Assertions.assertThat(actual).containsExactly(역삼역, 강남역, 교대역, 서초역);
	}
}