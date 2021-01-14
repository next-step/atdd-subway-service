package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/14
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
class PathFinderTest {
	@Mock
	private PathFinderRepository pathFinderRepository;

	private Station 강남역 = new Station("강남역");
	private Station 양재역 = new Station("양재역");
	private Station 교대역 = new Station("교대역");
	private Station 남부터미널역 = new Station("남부터미널역");

	private Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
	private Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 10);
	private Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

	@Test
	void findShortestPath(){
		//given
		SubwayPathSections 전체구간 = 구간_경로_조회(신분당선, 이호선, 삼호선);

		//when
		SubwayPath shortestPath = 최단경로_조회(전체구간, 교대역, 양재역);

		shortestPath.getStationsIds().forEach(ids -> System.out.println(ids));

		//then
		최단경로_구간_조회_응답(shortestPath, 교대역, 양재역);
	}

	private void 최단경로_구간_조회_응답(SubwayPath shortestPath, Station sourceStation, Station targetStation) {
		assertThat(shortestPath.getStationsIds()).containsExactly(sourceStation.getId(), targetStation.getId());
	}

	private SubwayPath 최단경로_조회(SubwayPathSections sections, Station sourceStation, Station targetStation) {
		PathFinder pathFinder = new PathFinder(pathFinderRepository);

		List<SubwayPathStation> subwayPathStations = new ArrayList<>();
		subwayPathStations.add(new SubwayPathStation(교대역));
		subwayPathStations.add(new SubwayPathStation(양재역));
		subwayPathStations.add(new SubwayPathStation(남부터미널역));
		int distance = 8;

		when(pathFinderRepository.getDijkstraShortestPath(sections, new SubwayPathStation(sourceStation), new SubwayPathStation(targetStation)))
			.thenReturn(new SubwayPath(subwayPathStations, distance));

		return pathFinder.findShortestPath(sections, new SubwayPathStation(sourceStation), new SubwayPathStation(targetStation));
	}

	private SubwayPathSections 구간_경로_조회(Line... lines) {
		List<SubwayPathSection> subwayPathSections = Arrays.stream(lines)
			.map(line -> line.getSections())
			.flatMap(Collection::stream)
			.map(section -> sectionToSubwayPathSection(section))
			.collect(Collectors.toList());
		return new SubwayPathSections(subwayPathSections);
	}

	private SubwayPathSection sectionToSubwayPathSection(Section section) {
		return new SubwayPathSection(section.getUpStation(), section.getDownStation(), section.getDistance());
	}
}