package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionTest {

	private Line line;
	private Station 양재역;
	private Station 강남역;
	private Section section;

	@BeforeEach
	void setUp() {
		this.line = new Line("신분당선", "red");
		this.강남역 = new Station("강남역");
		this.양재역 = new Station("양재역");
		this.section = new Section(this.line, this.양재역, this.강남역, 10);
	}

	@DisplayName("현재 역이 상행 구간과 연결 여부를 반환")
	@Test
	void testIsLinedUpSection() {
		boolean actual = this.section.isLinkedUpSection(this.강남역);
		assertThat(actual).isTrue();
	}

	@DisplayName("현재 구간이 하행 구간과 연결 여부를 반환")
	@Test
	void testIsLinkedDownSection() {
		boolean actual = this.section.isLinkedDownSection(this.양재역);
		assertThat(actual).isTrue();
	}

	@DisplayName("중간 구간 추가 가능한지 여부 반환")
	@Test
	void testIsBuildable() {
		Station station = new Station("test");
		boolean buildableUpSection = this.section.isBuildable(new Section(this.line, 양재역, station, 5));
		boolean buildableDownSection = this.section.isBuildable(new Section(this.line, station, 강남역, 5));

		assertThat(buildableUpSection).isTrue();
		assertThat(buildableDownSection).isTrue();
	}

}