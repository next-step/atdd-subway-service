package nextstep.subway.path.application;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathNavigator;
import nextstep.subway.path.domain.TotalFareCalculator;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final LineService lineService;
	private final StationService stationService;

	public PathService(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(LoginMember member, PathRequest request) {
		PathNavigator navigator = pathNavigator();
		Path path = path(request, navigator);
		Fare fare = fare(member, path);
		return PathResponse.from(path, fare);
	}

	private Fare fare(LoginMember member, Path path) {
		return TotalFareCalculator.of(member, path.distance(), path.sections())
			.fare();
	}

	private Path path(PathRequest request, PathNavigator navigator) {
		return navigator.path(
			station(request.getSource()),
			station(request.getTarget())
		);
	}

	private PathNavigator pathNavigator() {
		Lines lines = lineService.findAll();
		return PathNavigator.from(lines);
	}

	private Station station(long request) {
		return stationService.findById(request);
	}

	public boolean isInvalidPath(Station source, Station target) {
		PathNavigator navigator = pathNavigator();
		return navigator.isInvalidPath(source, target);
	}
}
