package nextstep.subway.path.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponseDto;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class Paths {

	private List<Section> sections;
	private Set<Station> stations;
	private DijkstraShortestPath dijkstraShortestPath;

	public Paths(List<Section> sections) {
		this.sections = registerSections(sections);
		this.dijkstraShortestPath = build(sections);
	}

	public PathResponseDto getPath(Station source, Station target, Fare fare) {
		validate(source, target);

		GraphPath graphPath = dijkstraShortestPath.getPath(source, target);
		if (graphPath == null) {
			throw new RuntimeException("경로가 존재하지 않습니다.");
		}

		List<Station> shortestPath = graphPath.getVertexList();
		List<StationResponse> paths = shortestPath.stream()
			  .map(StationResponse::of)
			  .collect(Collectors.toList());
		int distance = (int) graphPath.getWeight();

		fare.calculateFare(shortestPath, sections, distance);
		return new PathResponseDto(paths, distance, fare.getFare());
	}

	private void validate(Station source, Station target) {
		if (!stations.contains(source) || !stations.contains(target)) {
			throw new RuntimeException("구간정보에 등록된 출발역(도착역)이 없습니다.");
		}

		if (source.equals(target)) {
			throw new RuntimeException("출발역과 도착역이 같습니다.");
		}
	}

	private List<Section> registerSections(List<Section> sections) {
		if (sections == null || sections.isEmpty()) {
			throw new RuntimeException();
		}

		return sections;
	}

	private DijkstraShortestPath build(List<Section> sections) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
		this.stations = getStations(sections);

		addVertex(graph);
		addEdge(graph, sections);
		return new DijkstraShortestPath(graph);
	}

	private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
		stations.forEach(
			  station -> graph.addVertex(station)
		);
	}

	private void addEdge(WeightedMultigraph<Station, DefaultWeightedEdge> graph,
		  List<Section> sections) {
		sections.forEach(section -> {
			graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
		});
	}

	private Set<Station> getStations(List<Section> sections) {
		Set<Station> stations = new HashSet<>();
		sections.forEach(section -> {
			stations.add(section.getUpStation());
			stations.add(section.getDownStation());
		});

		return stations;
	}
}
