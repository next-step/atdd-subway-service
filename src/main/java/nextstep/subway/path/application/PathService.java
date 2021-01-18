package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.fare.FareBuilder;
import nextstep.subway.path.domain.fare.Money;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {
	private final LineService lineService;

	public PathService(final LineService lineService) {
		this.lineService = lineService;
	}

	public PathResponse findPath(LoginMember loginMember, PathRequest request) {
		Station source = lineService.findStationById(request.getSource());
		Station target = lineService.findStationById(request.getTarget());

		PathFinder finder = new PathFinder(lineService.findLineAll());
		finder.selectShortPath(source, target);

		Money fare = FareBuilder.calculate(loginMember, finder);
		return PathResponse.of(finder.stations(), finder.distance(), fare);
	}
}
