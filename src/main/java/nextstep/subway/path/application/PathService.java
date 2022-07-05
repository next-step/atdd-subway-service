package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.generic.domain.Age.AgeType;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(LoginMember loginMember, long sourceId, long targetId) {
        final Station sourceStation = stationService.findStationById(sourceId);
        final Station targetStation = stationService.findStationById(targetId);
        final Lines allLines = lineService.findAllLines();
        final PathFinder pathFinder = new PathFinder(allLines);
        final Sections sections = pathFinder.find(sourceStation, targetStation);
        final Lines lines = allLines.findLinesBySections(sections);

        if (loginMember.isNotLogin()) {
            return PathResponse.of(sections, lines.maxSurcharge());
        }

        return PathResponse.of(sections, lines.maxSurcharge(), loginMember.getAge().ageType().getDiscountRate());
    }

}
