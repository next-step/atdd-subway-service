package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final EntityManager entityManager;

    public PathService(StationService stationService, EntityManager entityManager) {
        this.stationService = stationService;
        this.entityManager = entityManager;
    }

    public PathResponse findShortestPath(long sourceId, long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        List<Section> sections = findSections();
        Lines lines = Lines.valueOf(sections.stream()
                .map(Section::line)
                .collect(Collectors.toSet()));
        GraphPath<Station, DefaultWeightedEdge> shortestPath = lines.shortestPath(source, target);
        List<Station> path = shortestPath.getVertexList();
        return PathResponse.of(path.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()), (int) shortestPath.getWeight());
    }

    private List<Section> findSections() {
        return entityManager.createQuery(
                "select s from Section s join fetch s.upStation join fetch s.downStation join fetch s.line",
                Section.class).getResultList();
    }
}
