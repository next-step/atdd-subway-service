package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

	private final StationService stationService;
	private final LineRepository lineRepository;
	private final MemberService memberService;

	public PathService(StationService stationService, LineRepository lineRepository, MemberService memberService) {
		this.stationService = stationService;
		this.lineRepository = lineRepository;
		this.memberService = memberService;
	}

	public PathResponse findShortestPath(long loginId, long source, long target) {
		Member member = memberService.findById(loginId);
		List<Line> lines = lineRepository.findAll();
		Station sourceStation = stationService.findById(source);
		Station targetStation = stationService.findById(target);
		PathFinder pathFinder = new PathFinder(lines);
		Path path = pathFinder.getShortestPath(member, sourceStation, targetStation);
		return PathResponse.of(path);
	}
}
