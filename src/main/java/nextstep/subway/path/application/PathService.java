package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

	private LineService lineService;
	private StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(Long source, Long target) {
		List<Line> lines = lineService.findAll();
		Station sourceStation = stationService.findStationById(source);
		Station targetStation = stationService.findStationById(target);

		return dijkstraShortestPath(lines, sourceStation, targetStation);
	}

	private PathResponse dijkstraShortestPath(List<Line> lines, Station sourceStation, Station targetStation) {
		WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		Path path = new Path(lines);

		List<Station> nodes = path.getNodes();
		nodes.stream().forEach(station -> graph.addVertex(station));

		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			Sections sections = line.getSections();
			sections.setEdgeWeight(graph);
		}

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<Station> stations = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
		List<StationResponse> stationResponses = stations.stream().map(StationResponse::of).collect(Collectors.toList());
		double shortestDistance = dijkstraShortestPath.getPath(sourceStation, targetStation).getWeight();

		return PathResponse.of(stationResponses, (int) shortestDistance);
	}


}
