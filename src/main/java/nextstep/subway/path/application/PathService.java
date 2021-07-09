package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
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
		PathFinder pathFinder = new PathFinder(lines);

		List<String> shortestPath = pathFinder.findPath(startStation, destinationStation);

		List<StationResponse> stationResponses = new ArrayList<>();
		for (String stationName : shortestPath) {
			stationResponses.add(StationResponse.of(stationService.findByName(stationName)));
		}
		return new PathResponse(stationResponses, pathFinder.findPathLength(startStation, destinationStation));
	}
}
