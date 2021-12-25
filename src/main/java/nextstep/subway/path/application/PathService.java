package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final StationService stationService;
	private final LineService lineService;

	public PathService(final StationService stationService,
		final LineService lineService) {

		this.stationService = stationService;
		this.lineService = lineService;
	}

	public PathResponse find(Long sourceId, Long targetId, LoginMember loggedMember) {
		Station source = stationService.findStationById(sourceId);
		Station target = stationService.findStationById(targetId);
		List<Line> lines = lineService.findAll();
		PathFinder pathFinder = PathFinder.of(lines);
		return pathFinder.findPath(source, target, loggedMember);
	}

}
