package nextstep.subway.path.application;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.fare.FareAgeRule;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import static nextstep.subway.fare.FareAgeRule.fareByAge;
import static nextstep.subway.fare.FareAgeRule.fareByDiscount;
import static nextstep.subway.fare.FareDistanceRule.findFareByDistance;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;

    public PathService(final StationService stationService, final LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findDijkstraPath(LoginMember loginMember, Long source, Long target) {
        Station startStation = stationService.findById(source);
        Station endStation = stationService.findById(target);

        PathFinder pathFinder = getPathFinder();

        List<Station> pathStations = pathFinder.findShortestPathStations(startStation, endStation);
        int distance = pathFinder.findShortestPathDistance(startStation, endStation);

        long fare = getFare(pathStations, distance);

        Optional<FareAgeRule> fareAgeRule = fareByAge(loginMember.getAge());
        fare = fareByDiscount(fareAgeRule, fare);

        return getPathResponse(pathStations, distance, fare);
    }

    private long getFare(List<Station> pathStations, int distance) {
        long lineMaxFare = 0;

        lineMaxFare = pathStations.stream()
            .skip(1)
            .map(station -> lineService.findSectionByStation(getPreStation(pathStations, station), station))
            .map(Section::getLine)
            .map(Line::getFare)
            .max(Long::compareTo)
            .get();

        return lineMaxFare + findFareByDistance(distance);
    }

    private Station getPreStation(List<Station> pathStations, Station station) {
        int index = pathStations.indexOf(station);
        return pathStations.get(index-1);
    }

    private PathResponse getPathResponse(List<Station> stations, int distance, long fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance, fare);
    }

    private PathFinder getPathFinder() {
        List<Station> stations = stationService.findAll();
        List<Section> sections = lineService.findAllSection();

        return new PathFinder(stations, sections);
    }
}
