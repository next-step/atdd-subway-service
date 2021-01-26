package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("Sections의 Domain 단위테스트")
public class SectionsTest {

	private final Station 교대역 = new Station("교대역");
	private final Station 남부터미널역 = new Station("남부터미널역");
	private final Station 양재역 = new Station("양재역");

	private final Line 이호선 = new Line("이호선", "bg-red-600");
	private final Line 삼호선 = new Line("삼호선", "bg-red-600");

	private Sections sections;

	@BeforeEach
	void addSection() {
		sections = new Sections();
		sections.addSection(이호선, 교대역, 양재역, 3);
		sections.addSection(삼호선, 교대역, 양재역, 5);
		sections.addSection(삼호선, 교대역, 남부터미널역, 3);
	}

	@Test
	void getStations() {
		assertThat(sections.getSortedStations(삼호선)).containsExactly(교대역, 남부터미널역, 양재역);
	}

	@Test
	void removeSection() {
		sections.removeStation(삼호선, 남부터미널역);
		assertThat(sections.getSortedStations(삼호선)).containsExactly(교대역, 양재역);
	}

	@Test
	void findLineWithMinDistance() {
		assertThat(sections.findLineWithMinDistance(교대역, 양재역)).hasValue(이호선);
	}
}
