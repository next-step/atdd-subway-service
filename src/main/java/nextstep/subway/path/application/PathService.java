package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathException;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public Path findPath(Long sourceStationId, Long targetStationId) {
		List<Line> lines = lineRepository.findAll();
		Station source = stationRepository.findById(sourceStationId)
			.orElseThrow(() -> new PathException("출발역이 존재하지 않습니다."));
		Station target = stationRepository.findById(targetStationId)
			.orElseThrow(() -> new PathException("도착역이 존재하지 않습니다."));
		PathFinder pathFinder = PathFinder.of(lines);
		return pathFinder.findPath(source, target);
	}
}
