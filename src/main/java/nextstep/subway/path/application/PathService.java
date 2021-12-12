package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {

	private final StationRepository stationRepository;
	private final LineRepository lineRepository;

	public PathService(final StationRepository stationRepository, final LineRepository lineRepository) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
	}

	public PathResponse find(Long sourceId, Long targetId) {
		Station source = getStationById(sourceId);
		Station target = getStationById(targetId);
		List<Line> lines = lineRepository.findAll();
		PathFinder pathFinder = PathFinder.ofLines(lines);
		return pathFinder.findPath(source, target);
	}

	private Station getStationById(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() ->
			new AppException(ErrorCode.NOT_FOUND, "Station(id:{}) 를 찾을 수 없습니다", stationId));
	}

}
