package nextstep.subway.path.application;

import nextstep.subway.exception.NoLoginUserException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathFinderUsingWeightedMultigraph;
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

    @Transactional(readOnly = true)
    public PathResponse findPath(Long loginMemberId, Long sourceStationId, Long targetStationId) {
        ShortestPath shortestPath = findShortestPath(sourceStationId, targetStationId);

        if (loginMemberId != null) {
            return PathResponse.of(shortestPath.withUser(
                    memberRepository.findById(loginMemberId).orElseThrow(NoLoginUserException::new))
            );
        }
        return PathResponse.of(shortestPath.withoutUser());
    }

    @Transactional(readOnly = true)
    protected ShortestPath findShortestPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(NoSuchElementException::new);
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinderUsingWeightedMultigraph(lines);
        return pathFinder.executeDijkstra(sourceStation, targetStation);
    }
}
