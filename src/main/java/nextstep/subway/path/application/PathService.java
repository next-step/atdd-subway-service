package nextstep.subway.path.application;

import java.util.NoSuchElementException;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    public PathService(StationRepository stationRepository,
        LineRepository lineRepository, MemberRepository memberRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    public PathResponse findShortestPath(long memberId, long source, long target) {
        Station sourceStation = stationRepository.findById(source)
            .orElseThrow(() -> new NoSuchElementException("출발역 정보 없음"));
        Station targetStation = stationRepository.findById(target)
            .orElseThrow(() -> new NoSuchElementException("도착역 정보 없음"));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NoSuchElementException("회원 정보 없음"));

        Path path = new PathFinder(lineRepository.findAll()).getPath(sourceStation, targetStation);
        Distance shortestDistance = path.getShortestDistance();
        Fare fare = Fare.calculateByPolicy(path, shortestDistance.distance(), member.getAge());
        
        return PathResponse.from(path, shortestDistance, fare);
    }
}
