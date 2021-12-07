package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.CanNotFindPathException;
import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional
public class PathService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public PathService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional(readOnly = true)
	public PathResponse findPath(PathRequest request, LoginMember loginMember) {
		Station source = stationRepository.findById(request.getSource()).orElseThrow(CanNotFindPathException::new);
		Station target = stationRepository.findById(request.getTarget()).orElseThrow(CanNotFindPathException::new);
		List<Line> lines = lineRepository.findAll();

		PathFinder pathFinder = PathFinder.of(lines);
		Path path = pathFinder.find(source, target);
		FarePolicy farePolicy = new FarePolicy();
		int fare;
		if (loginMember.isLogin()) {
			fare = farePolicy.calculateBy(path.getDistance(), path.getLines(), loginMember.getAge());
		} else {
			fare = farePolicy.calculateBy(path.getDistance(), path.getLines());
		}

		return PathResponse.of(path, fare);
	}
}
