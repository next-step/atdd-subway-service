package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public PathResponse findPathByIds(Long userid, Long sourceId, Long targetId) {
        if(sourceId.equals(targetId)) {
            throw new IllegalArgumentException("동일한 ID를 입력했습니다.");
        }
        Member member = memberService.getMember(userid);
        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.initialPathFinder(lines);
        GraphPath path = pathFinder.getShortestPath(sourceId, targetId);
        if(Objects.isNull(path)) {
            throw new IllegalArgumentException("경로를 찾지 못하였습니다.");
        }
        Fare fare = pathFinder.getFare(path.getWeight());
        fare.discountPerAge(member.getAge());

        return PathResponse.of(getStationsById(path.getVertexList()), path.getWeight(), fare.getFare());
    }

    private List<Station> getStationsById(List<Long> ids) {
        return ids.stream()
                .map(id -> stationService.findStationById(id))
                .collect(Collectors.toList());
    }
}
