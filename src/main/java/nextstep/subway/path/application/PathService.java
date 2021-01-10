package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {
	private final SectionRepository sectionRepository;

	public PathResponse findShortestPath(Station sourceStation, Station targetStation) {
		if (sourceStation == targetStation) {
			throw new PathFindException("source station and target station are the same");
		}

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

		Sections sections = new Sections(sectionRepository.findAll());

		Stations stations = sections.getStations();
		checkStationIsInSections(sourceStation, stations);
		checkStationIsInSections(targetStation, stations);

		stations.forEach(graph::addVertex);
		sections.forEach(section -> graph.setEdgeWeight(
			graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));


		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> shortestPath
			= dijkstraShortestPath.getPath(sourceStation, targetStation);

		if (shortestPath == null) {
			throw new PathFindException("source station is not connected to target station");
		}

		return PathResponse.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
	}

	private void checkStationIsInSections(Station sourceStation, Stations stations) {
		if (!stations.contains(sourceStation)) {
			throw new PathFindException("the station is not in the sections: " + sourceStation);
		}
	}
}
