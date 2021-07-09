package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@Service
public class PathService {
	private LineRepository lineRepository;
	private StationService stationService;

	public PathService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public PathResponse findPath(Long sourceId, Long targetId) {
		List<Line> lines = lineRepository.findAll();
		Station startStation = stationService.findStationById(sourceId);
		Station destinationStation = stationService.findStationById(targetId);
		WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

		for (Line line : lines) {
			for (Station station : line.getStations()) {
				graph.addVertex(station.getName());
			}

			for (Section section : line.getSection()) {
				graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
			}
		}

		DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
		List<String> shortestPath = dijkstraShortestPath.getPath(startStation.getName(), destinationStation.getName()).getVertexList();

		List<StationResponse> stationResponses = new ArrayList<>();
		for (String stationName : shortestPath) {
			stationResponses.add(StationResponse.of(new Station(stationName)));
		}
		return new PathResponse(stationResponses, (int) dijkstraShortestPath.getPathWeight(startStation.getName(), destinationStation.getName()));
	}
}
