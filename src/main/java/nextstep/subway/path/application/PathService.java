package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PathService {

	private LineRepository lineRepository;
	private StationService stationService;

	public PathService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(PathRequest request) {
		List<Station> stations = stationService.findAllByIdIn(Arrays.asList(request.getSource(), request.getTarget()));

		PathFinder pathFinder = new PathFinder(lineRepository.findAll(), stations);

		GraphPath<Station, DefaultWeightedEdge> graphPath = pathFinder.findShortestPath(stations);
		return PathResponse.of(graphPath.getVertexList(), (int) graphPath.getWeight());
	}
}
