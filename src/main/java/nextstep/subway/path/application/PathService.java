package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.AgeType;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PriceCalculator;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final SectionService sectionService;

    public PathService(StationService stationService, SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    public PathResponse findPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        Sections sections = sectionService.findAll();
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);

        PathFinder pathFinder = PathFinder.of(sections);
        ShortestPath shortestPath = pathFinder.findShortestPath(sourceStation, targetStation);
        int price = PriceCalculator.process(AgeType.findType(loginMember.getAge()), shortestPath);

        return PathResponse.of(shortestPath, price);
    }

    public boolean validatePath(Station sourceStation, Station targetStation) {
        Sections sections = sectionService.findAll();
        PathFinder pathFinder = PathFinder.of(sections);

        return pathFinder.isValidatePath(sourceStation, targetStation);
    }
}
