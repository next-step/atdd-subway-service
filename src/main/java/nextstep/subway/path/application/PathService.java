package nextstep.subway.path.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.price.PathPrice;
import nextstep.subway.path.domain.price.BasicPriceCalculator;
import nextstep.subway.path.domain.route.PathRoute;
import nextstep.subway.path.domain.price.PriceCalculator;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

	private final PathFinder pathFinder;
	private final LineService lineService;
	private final StationService stationService;

	public PathService(PathFinder pathFinder, LineService lineService,
		StationService stationService) {
		this.pathFinder = pathFinder;
		this.lineService = lineService;
		this.stationService = stationService;
	}

	public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
		Station departStation = stationService.findById(source);
		Station arriveStation = stationService.findById(target);

		List<Line> lines = lineService.findAllExistStations(Arrays.asList(departStation, arriveStation));
		PathRoute shortestPathRoute = pathFinder.findShortestPath(lines, departStation, arriveStation);
		PathPrice pathPrice = PathPrice.calculatePrice(shortestPathRoute.getDistance(), getPriceCalculator(),
			loginMember.getAge());

		pathPrice.addLineExtraPrice(shortestPathRoute.getUsedLines());
		Path path = new Path(shortestPathRoute, pathPrice);
		return PathResponse.of(path);
	}

	private PriceCalculator getPriceCalculator() {
		return new BasicPriceCalculator();
	}
}