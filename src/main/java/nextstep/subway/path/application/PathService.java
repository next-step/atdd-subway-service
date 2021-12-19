package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.JgraphtPathFinder;
import nextstep.subway.path.domain.PathFinderInterface;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberRepository memberRepository;

    public PathService(LineService lineService, StationService stationService, MemberRepository memberRepository) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public PathResponse findShortestPath(PathRequest request, Long memberId) {
        List<Line> lines = lineService.findLines();
        PathFinderInterface pathFinder = new JgraphtPathFinder(lines);
        Path path = findShortestPath(request, pathFinder);
        Fare fare = FareCalculator.calculateFare(path, findMemberById(memberId));
        return PathResponse.of(path, fare);
    }

    public Path findShortestPath(PathRequest request,
        PathFinderInterface pathFinder) {
        Station srcStation = stationService.findById(request.getSrcStationId());
        Station destStation = stationService.findById(request.getDestStationId());

        if (srcStation.equals(destStation)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }

        if (!pathFinder.containsStation(srcStation) || !pathFinder.containsStation(destStation)) {
            throw new IllegalArgumentException("출발역이나 도착역이 노선 내에 존재하지 않습니다.");
        }

        if (!pathFinder.stationsConnected(srcStation, destStation)) {
            throw new RuntimeException("출발역과 도착역이 연결이 되어 있지 않습니다.");
        }
        
        return pathFinder.getShortestPath(srcStation, destStation);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(AuthorizationException::new);
    }
}
