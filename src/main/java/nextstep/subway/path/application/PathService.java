package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {

	private final LineRepository lineRepository;
	private final StationService stationService;

	public PathService(final LineRepository lineRepository, final StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public PathResponse getShortestPath(PathRequest pathRequest) {
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = stationService.findStationById(pathRequest.getSource());
		Station targetStation = stationService.findStationById(pathRequest.getTarget());

		Path shortestPath = Path.of(lines, sourceStation, targetStation);
		return PathResponse.from(shortestPath);
	}
}
