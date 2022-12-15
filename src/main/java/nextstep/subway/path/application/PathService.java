package nextstep.subway.path.application;

import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PathService {

    private static final String MESSAGE_SOURCE_TARGET_SHOULD_BE_DIFFERENT = "출발역과 도착역은 달라야 합니다";
    private static final String MESSAGE_SOURCE_TARGET_WRONG_VALUE = "비정상적인 값이 전달되었습니다";

    private final StationService stationService;
    private final SectionRepository sectionRepository;
    private final PathFindAlgorithm pathFinder;
    private final MemberService memberService;


    public PathService(StationService stationService, SectionRepository sectionRepository, PathFindAlgorithm pathFinder,
                       MemberService memberService) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
        this.pathFinder = pathFinder;
        this.memberService = memberService;
    }

    public PathResponse findShortestPath(Long loginId, Long source, Long target) {
        validateInput(source, target);
        Station departStation = stationService.findStationById(source);
        Station destStation = stationService.findStationById(target);
        List<Section> allSections = sectionRepository.findAll();
        ShortestPath shortestGraph = PathFinder.of(allSections, this.pathFinder)
            .getShortestGraph(departStation, destStation);

        Member memberEntity = memberService.findMemberEntity(loginId);
        int fare = FareCalculator.calculate(shortestGraph.getPathDistance(),
            shortestGraph.getMaxLineFare(), memberEntity.getAge());
        return PathResponse.from(shortestGraph, fare);
    }

    private static void validateInput(Long sourceStationId, Long targetStationId) {
        if (Objects.isNull(sourceStationId) || Objects.isNull(targetStationId) || sourceStationId <= 0 || targetStationId <= 0) {
            throw new IllegalArgumentException(MESSAGE_SOURCE_TARGET_WRONG_VALUE);
        }
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException(MESSAGE_SOURCE_TARGET_SHOULD_BE_DIFFERENT);
        }
    }
}
