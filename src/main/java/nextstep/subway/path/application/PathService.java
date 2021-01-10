package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final StationService stationService;

	public PathService(final StationService stationService) {
		this.stationService = stationService;
	}

	public PathResponse findPath(PathRequest request) {
		Station sourceStation = stationService.findStationById(request.getSource());
		Station targetStation = stationService.findStationById(request.getTarget());

		PathFinder.start(sourceStation, targetStation);
		return null;
	}
}
