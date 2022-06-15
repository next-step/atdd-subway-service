package nextstep.subway.path.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final EntityManager entityManager;

    public PathService(StationService stationService, PathFinder pathFinder, EntityManager entityManager) {
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.entityManager = entityManager;
    }

    public PathResponse findShortestPath(LoginMember loginMember, long sourceId, long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        pathFinder.initGraph(findLines());
        Path path = pathFinder.shortestPath(source, target);
        Fare fare = FareCalculator.calculateFare(path.findPathLinesFrom(findSections()), path, loginMember.getAge());
        return PathResponse.of(path.stations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), path.distanceValue(), fare.fare());
    }

    private Set<Line> findLines() {
        return findSections().stream()
                .map(Section::line)
                .collect(Collectors.toSet());
    }

    private List<Section> findSections() {
        return entityManager.createQuery(
                "select s from Section s join fetch s.upStation join fetch s.downStation join fetch s.line",
                Section.class).getResultList();
    }
}
