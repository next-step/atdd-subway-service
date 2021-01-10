package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.application.SectionRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {
	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;

	public PathResponse findShortestPath(Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph
			= new WeightedMultigraph<>(DefaultWeightedEdge.class);

		List<Station> stations = stationRepository.findAll();
		for (Station station : stations) {
			graph.addVertex(station);
		}

		List<Section> sections = sectionRepository.findAll();
		for (Section section : sections) {
			System.out.println(section.toString());
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		}

		DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
		GraphPath<Station, DefaultWeightedEdge> shortestPath
			= dijkstraShortestPath.getPath(sourceStation, targetStation);

		return PathResponse.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
	}
}
