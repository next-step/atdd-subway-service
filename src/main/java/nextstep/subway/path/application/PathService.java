package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.SectionException;
import nextstep.subway.exception.error.ErrorCode;
import nextstep.subway.line.domain.section.Section;
import nextstep.subway.line.domain.section.SectionRepository;
import nextstep.subway.path.domain.FareCalculate;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, Long sourceId, Long targetId) {
        PathFinder pathFinder = getPathFinder();
        Station source = stationService.findStationById(sourceId);
        Station target = stationService.findStationById(targetId);

        List<Station> dijkstraPath = pathFinder.getDijkstraShortestPath(source, target);
        int sumDistance = pathFinder.getSumLineStationsDistance(source, target);
        BigDecimal pareMoney = FareCalculate.getPareMoney(loginMember, pathFinder.getGraphPath(source, target), sumDistance);

        return PathResponse.of(dijkstraPath, sumDistance, pareMoney);
    }

    private PathFinder getPathFinder() {
        List<Section> sections = sectionRepository.findAll();
        if (sections.size() == 0) {
            throw new SectionException(ErrorCode.NOT_FOUND_ENTITY, "전 노선에 구간이 존재하지 않습니다.");
        }
        return new PathFinder(sections);
    }
}
