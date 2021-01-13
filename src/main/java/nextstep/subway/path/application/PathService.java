package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.fare.Fare;
import nextstep.subway.fare.FarePolicyByDistance;
import nextstep.subway.fare.FarePolicyByPassenger;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathFinderResponse findShortestPath(final FarePolicyByPassenger farePolicyByPassenger, final long departureId, final long arrivalId) {
        final Station departureStation = stationRepository.findById(departureId).orElseThrow(NotFoundException::new);
        final Station arrivalStation = stationRepository.findById(arrivalId).orElseThrow(NotFoundException::new);

        final List<Section> allSections = lineRepository.findAll().stream()
            .flatMap(line -> line.getSections().stream())
            .collect(Collectors.toList());
        final PathFinder pathFinder = PathFinder.of(allSections);
        final GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(departureStation, arrivalStation);

        final Fare fare = getFare(farePolicyByPassenger, allSections, shortestPath);

        return PathFinderResponse.of(shortestPath, fare.getFare());
    }

    private Fare getFare(final FarePolicyByPassenger farePolicyByPassenger, final List<Section> allSections, final GraphPath<Station, DefaultWeightedEdge> shortestPath) {
        final int travelDistance = (int) shortestPath.getWeight();
        final List<Station> stations = shortestPath.getVertexList();

        final int lineFare = FarePolicyByDistance.getAdditionalFareByDistance(travelDistance).getFare();
        final int highestFareByLineFare = findHighestFareByLine(allSections, stations).getFare();

        final Fare finalFare = Fare.createBaseFare()
            .plus(lineFare)
            .plus(highestFareByLineFare);

        return farePolicyByPassenger.discountByPassengerType(finalFare);
    }

    private Fare findHighestFareByLine(final List<Section> sections, final List<Station> stations) {
        final List<Section> foundSections = sections.stream()
            .filter(section -> section.findMatchingSection(stations))
            .collect(Collectors.toList());

        final int maxFare = foundSections.stream()
            .map(Section::getAdditionalFareLine)
            .mapToInt(fare -> fare)
            .max()
            .orElseThrow(NotFoundException::new);

        return Fare.of(maxFare);
    }
}
