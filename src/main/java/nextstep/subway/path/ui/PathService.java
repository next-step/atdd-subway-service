package nextstep.subway.path.ui;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
	private final LineRepository lineRepository;
	private final StationService stationService;

	public PathService(LineRepository lineRepository, StationService stationService) {
		this.lineRepository = lineRepository;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public PathResponse findPath(PathRequest request) {
		List<Line> lines = lineRepository.findAll();
		Station source = stationService.findStationById(request.getSource());
		Station target = stationService.findStationById(request.getTarget());
		PathFinder pathFinder = PathFinder.of(lines);
		Path path = pathFinder.find(source, target);
		return PathResponse.of(path);
	}
}
