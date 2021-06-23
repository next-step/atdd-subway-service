package nextstep.subway.path.application;

import nextstep.subway.exception.NoLoginUserException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, MemberRepository memberRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

//    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
//        ShortestPath shortestPath = findShortestPath(sourceStationId, targetStationId);
//        return PathResponse.of(shortestPath);
//    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long loginMemberId, Long sourceStationId, Long targetStationId) {
        ShortestPath shortestPath = findShortestPath(sourceStationId, targetStationId);

        Member member = null;
        if (loginMemberId != null) {
            member = memberRepository.findById(loginMemberId)
                    .orElseThrow(NoLoginUserException::new);
        }
        return PathResponse.of(shortestPath.withUser(member));
    }

    @Transactional(readOnly = true)
    ShortestPath findShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(NoSuchElementException::new);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        return pathFinder.executeDijkstra(sourceStation, targetStation);
    }
}
