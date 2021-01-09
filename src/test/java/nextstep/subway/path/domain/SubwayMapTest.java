package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class SubwayMapTest {
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
		이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
		삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);
	}

	@Test
	@DisplayName("전체 노선의 전체 구간을 조회하면 노선에 등록된 모든 구간이 조회되어야한다.")
	void registerGraph() {
		//given
		Section section1 = new Section(신분당선, 강남역, 양재역, 10);
		Section section2 = new Section(이호선, 교대역, 강남역, 10);
		Section section3 = new Section(삼호선, 교대역, 양재역, 5);
		SubwayMap lines = new SubwayMap(Arrays.asList(신분당선, 이호선, 삼호선));

		//when
		List<Section> allSections = lines.allSections();

		//then
		assertThat(allSections).contains(section1, section2, section3);
	}
}
