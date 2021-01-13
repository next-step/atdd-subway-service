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

import static java.util.stream.Collectors.*;

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
        final Station departureStation = stationRepository.findById(departureId)
            .orElseThrow(NotFoundException::new);
        final Station arrivalStation = stationRepository.findById(arrivalId)
            .orElseThrow(NotFoundException::new);
        final List<Section> allSections = lineRepository.findAll().stream()
            .flatMap(line -> line.getSections().stream())
            .collect(toList());

        final PathFinder pathFinder = PathFinder.of(allSections);
        final GraphPath<Station, DefaultWeightedEdge> shortestPath = pathFinder.findShortestPath(departureStation, arrivalStation);

        final List<Section> foundSections = findSectionsIncludedInTheShortestPath(allSections, shortestPath.getVertexList());
        final Fare fare = getFare(farePolicyByPassenger, foundSections, (int) shortestPath.getWeight());

        return PathFinderResponse.of(shortestPath, fare.getFare());
    }

    private Fare getFare(final FarePolicyByPassenger farePolicyByPassenger, final List<Section> foundSections, final int travelDistance) {
        final int additionalFareByDistance = FarePolicyByDistance.getAdditionalFareByDistance(travelDistance).getFare();
        final int highestAdditionalFareByLine = findHighestFareByLine(foundSections).getFare();

        final Fare finalFare = Fare.createBaseFare()
            .plus(additionalFareByDistance)
            .plus(highestAdditionalFareByLine);

        return farePolicyByPassenger.discountByPassengerType(finalFare);
    }

    private Fare findHighestFareByLine(final List<Section> foundSections) {
        final int maxFare = foundSections.stream()
            .mapToInt(Section::getAdditionalFareLine)
            .max()
            .orElseThrow(NotFoundException::new);

        return Fare.of(maxFare);
    }

    private List<Section> findSectionsIncludedInTheShortestPath(final List<Section> allSections, final List<Station> vertexList) {
        return allSections.stream()
            .filter(section -> section.newFindMatchingSection(vertexList))
            .collect(toList());
    }
}
