package nextstep.subway.path.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
	private LineRepository lineRepository;
	private StationService stationService;

	public PathService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public PathResponse findPath(Long sourceId, Long targetId) {
		List<Line> lines = lineRepository.findAll();
		Station startStation = stationService.findStationById(sourceId);
		Station destinationStation = stationService.findStationById(targetId);
		PathFinder pathFinder = new PathFinder(lines);

		return new PathResponse(getStations(pathFinder.findPath(startStation, destinationStation)), pathFinder.findPathLength(startStation, destinationStation));
	}

	private List<Station> getStations(List<String> shortestPath) {
		List<Station> stationResponses = new ArrayList<>();
		for (String stationName : shortestPath) {
			stationResponses.add(stationService.findByName(stationName));
		}

		return stationResponses;
	}
}
