package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathCalculateRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

	private final LineRepository lines;
	private final StationRepository stations;

	public PathService(LineRepository lines, StationRepository stations) {
		this.lines = lines;
		this.stations = stations;
	}

	public PathResponse calculatePath(PathCalculateRequest pathCalculateRequest) {
		Station source = stations.findById(pathCalculateRequest.getSourceStationId())
				.orElseThrow(() -> new IllegalArgumentException(""));
		Station target = stations.findById(pathCalculateRequest.getTargetStationId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		List<Line> allLines = lines.findAll();

		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
		allLines.forEach(line -> addLineToGraph(graph, line));

		List<Station> stations = new DijkstraShortestPath<>(graph).getPath(source, target).getVertexList();
		return PathResponse.of(stations);
	}

	private void addLineToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
		line.getSections().forEachRemaining(section -> {
			graph.addVertex(section.getUpStation());
			graph.addVertex(section.getDownStation());
			DefaultWeightedEdge edge = graph.addEdge(section.getUpStation(), section.getDownStation());
			graph.setEdgeWeight(edge, section.getDistance().getWeight());
		});
	}
}
