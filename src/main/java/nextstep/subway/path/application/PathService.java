package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NoDataException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class PathService {

	private StationRepository stationRepository;
	private LineService lineService;

	public PathService(StationRepository stationRepository, LineService lineService) {
		this.stationRepository = stationRepository;
		this.lineService = lineService;
	}

	public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
		Station sourceStation = stationRepository.findById(source)
			.orElseThrow(NoDataException::new);
		Station targetStation = stationRepository.findById(target)
			.orElseThrow(NoDataException::new);

		List<Line> lines = lineService.fineLinesByStations(sourceStation, targetStation);
		PathFinder pathFinder = new PathFinder(sourceStation, targetStation, lines);
		return pathFinder.findShortestPathPathResponse();
	}
}
