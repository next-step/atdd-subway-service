package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class PathFinderTest {
	private static Station 강남역 = new Station(1L, "강남역");
	private static Station 역삼역 = new Station(2L, "역삼역");
	private static Station 선릉역 = new Station(3L, "선릉역");
	private static Section 첫번째_구간 = new Section(null, null, 강남역, 역삼역, Distance.of(10));
	private static Section 두번째_구간 = new Section(null, null, 역삼역, 선릉역, Distance.of(10));
	private static Section 세번째_구간 = new Section(null, null, 강남역, 선릉역, Distance.of(100));
	private static PathFinder pathFinder;

	@BeforeEach
	public void setUp() {
		ArrayList<Station> stations = Lists.newArrayList(강남역, 역삼역, 선릉역);
		ArrayList<Section> sections = Lists.newArrayList(첫번째_구간, 두번째_구간, 세번째_구간);
		pathFinder = PathFinder.create(stations, sections);
	}

	@Test
	@DisplayName("PathFinder 생성")
	public void PathFinderTest() {
		//then
		assertThat(pathFinder).isNotNull();
	}

	@Test
	@DisplayName("PathFinder 최단경로 순서 별 역 가져오기")
	public void PathFinderFindShortestPathVertexesTest() {
		//given
		//when
		List<Station> shortestPathVertexes = pathFinder.findShortestPathVertexes(선릉역, 강남역);
		//then
		assertThat(shortestPathVertexes).containsExactly(선릉역, 역삼역, 강남역);
	}

	@Test
	@DisplayName("PathFinder 최단경로 길이 가져오기")
	public void PathFinderFindShortestPathDistanceTest() {
		//given
		//when
		Distance shortestPathDistance = pathFinder.findShortestPathDistance(선릉역, 강남역);
		System.out.println(shortestPathDistance.value());
		//then
		assertThat(shortestPathDistance).isEqualTo(Distance.of(20));
	}

	@Test
	@DisplayName("PathFinder 최단경로 구간 가져오기")
	public void PathFinderFindShortestPathEdgesTest() {
		//given
		//when
		List<SectionEdge> shortestPathVertexes = pathFinder.findShortestPathEdges(선릉역, 강남역);
		//then
		assertThat(shortestPathVertexes).containsExactly(new SectionEdge(두번째_구간), new SectionEdge(첫번째_구간));
	}

}

