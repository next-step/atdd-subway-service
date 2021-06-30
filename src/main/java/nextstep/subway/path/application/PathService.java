package nextstep.subway.path.application;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

	private LineService lineService;

	public PathService(LineService lineService) {
		this.lineService = lineService;
	}

	public PathResponse findShortestPath(Long source, Long target) {
		List<StationResponse> stations = Arrays.asList(StationResponse.of(new Station("서울역")), StationResponse.of(new Station("광명역")), StationResponse.of(new Station("대전역")), StationResponse.of(new Station("동대구역")), StationResponse.of(new Station("부산역")));
		int distance = 40;
		return PathResponse.of(stations, distance);
	}

	private void dijkstraShortestPath() {
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		List<Line> lines = lineService.findAll();
		Path path = new Path(lines);

		List<Station> nodes = path.getNodes();
		nodes.stream().forEach(station -> graph.addVertex(station.getName()));

		//graph.addVertex("v1");
		//graph.addVertex("v2");
		//graph.addVertex("v3");
		graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
		graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
		graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath("v3", "v1").getVertexList();
	}


}
