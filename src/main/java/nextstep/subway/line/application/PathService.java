package nextstep.subway.line.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Paths;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findPaths(LoginMember loginMember, Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Section> sections = lineService.findAllSections();

        Paths paths = new Paths(new Sections(sections));
        Fare fare = new Fare(loginMember.getAgePolicy());
        return paths.getShortestPath(sourceStation, targetStation, fare);
    }
}
