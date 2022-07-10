package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.charge.domain.Charge;
import nextstep.subway.charge.domain.ChargeCalculator;
import nextstep.subway.line.application.SectionService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final SectionService sectionService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(SectionService sectionService, StationService stationService, PathFinder pathFinder) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId, LoginMember loginMember) {
        List<Section> allSections = sectionService.findAllSections();
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);

        Path path = pathFinder.findShortestPath(allSections, source, target);
        Charge charge = calculateCharge(loginMember.getAge(), path.getDistance(),
                path.getPathRouteSections(allSections));
        path.updateCharge(charge);

        return PathResponse.of(path);
    }

    private Charge calculateCharge(Integer age, Integer totalDistance, List<Section> sections) {
        ChargeCalculator chargeCalculator = new ChargeCalculator(age, totalDistance, sections);
        return chargeCalculator.calculate();
    }

}
