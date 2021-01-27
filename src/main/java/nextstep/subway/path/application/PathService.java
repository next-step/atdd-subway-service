package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.ShortestPath;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {
	public static final String SAME_SOURCE_AND_TARGET = "출발역과 도착역이 같습니다.";
	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public PathResponse findShortestPath(LoginMember loginMember, PathRequest pathRequest) {
		validatePathRequest(pathRequest);

		Station source = stationService.findStationById(pathRequest.getSource());
		Station target = stationService.findStationById(pathRequest.getTarget());

		PathFinder pathFinder = new PathFinder(lineService.findAll());
		ShortestPath shortestPath = pathFinder.findShortestPath(source.getId(), target.getId());

		List<Station> stations = stationService.findAllStationsByIds(shortestPath.getStationIds());
		List<Line> lines = lineService.findAllLinesByIds(shortestPath.getLineIds());
		Path path = Path.of(stations, shortestPath.getDistance(), lines, loginMember.getAge());

		return PathResponse.of(path);
	}

	private void validatePathRequest(PathRequest request) {
		Long sourceStationId = request.getSource();
		Long targetStationId = request.getTarget();

		if (sourceStationId.equals(targetStationId)) {
			throw new IllegalArgumentException(SAME_SOURCE_AND_TARGET);
		}
	}
}
