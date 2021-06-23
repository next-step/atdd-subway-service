package nextstep.subway.line.application;

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
    private final SectionRepository sectionRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        PathFinder pathFinder = initPathFinder();

        validation(sourceStationId, targetStationId);
        Station sourceStation = findStation(sourceStationId);
        Station targetStation = findStation(targetStationId);

        List<Station> stations = pathFinder.getPath(sourceStation, targetStation);

        int extraFare = getMaxExtraFare(stations);

        List<StationResponse> stationResponses = stations
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = pathFinder.getWeight(sourceStation, targetStation);
        return new PathResponse(stationResponses, distance, new Fare(distance, extraFare));
    }

    private int getMaxExtraFare(List<Station> stations) {
        int extraFare = 0;

        List<Section> sections = sectionRepository.findAll();
        for (int i = 1; i < stations.size(); i++) {
            Station prevStation = stations.get(i - 1);
            Station nextStation = stations.get(i);

            extraFare = Math.max(getMaxExtraSectionLine(sections, prevStation, nextStation).getExtraFare(), extraFare);
        }

        return extraFare;
    }

    private Line getMaxExtraSectionLine(List<Section> sections, Station prevStation, Station nextStation) {
        Section maxExtraFareSection = sections.stream()
                .filter(section -> section.isSameSection(prevStation, nextStation))
                .max(Comparator.comparing(section -> section.getLine().getExtraFare()))
                .orElseThrow(() -> new NoSuchElementException("일치하는 구간이 없습니다."));
        return maxExtraFareSection.getLine();
    }

    private void validation(Long sourceStationId, Long targetStationId) {
        if (sourceStationId == targetStationId) {
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
