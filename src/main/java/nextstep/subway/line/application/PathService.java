package nextstep.subway.line.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.*;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(LoginMember loginMember, Long sourceStationId, Long targetStationId) {
        PathFinder pathFinder = initPathFinder();

        validation(sourceStationId, targetStationId);
        Station sourceStation = findStation(sourceStationId);
        Station targetStation = findStation(targetStationId);

        List<Station> stations = pathFinder.getPath(sourceStation, targetStation);

        List<SectionWeightedEdge> sectionWeightEdge = pathFinder.getSectionWeightEdge(sourceStation, targetStation);
        int extraFare = getMaximumExtraFareFromLineEdge(sectionWeightEdge);

        List<StationResponse> stationResponses = makeStationResponse(stations);

        int distance = pathFinder.getWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, distance,
                new Fare(distance, extraFare, FareByAge.of(loginMember.getAge()).fare()));
    }

    private List<StationResponse> makeStationResponse(List<Station> stations) {
        return stations
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private Integer getMaximumExtraFareFromLineEdge(List<SectionWeightedEdge> sectionWeightEdge) {
        return sectionWeightEdge.stream()
                .max(Comparator.comparing(sectionWeightedEdge -> sectionWeightedEdge.getExtraFare()))
                .map(sectionWeightedEdge -> sectionWeightedEdge.getExtraFare())
                .orElse(0);
    }

    private void validation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId.equals(targetStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같을수는 없습니다.");
        }
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 역입니다."));
    }

    private PathFinder initPathFinder() {
        Lines lines = new Lines(lineRepository.findAll());
        Stations stations = new Stations(stationRepository.findAll());

        return new PathFinder(lines, stations);
    }
}
