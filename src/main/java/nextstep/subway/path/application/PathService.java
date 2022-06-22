package nextstep.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SubwayGraph;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.station.application.StationService;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private final SectionService sectionService;
    private final StationService stationService;

    public PathService(final SectionService sectionService, final StationService stationService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(final Long sourceStationId, final Long targetStationId) {
        final SubwayGraph subwayGraph = new SubwayGraph(
                sectionService.findAllSections(),
                stationService.findAllStations());
        final Path path = subwayGraph.findShortestPath(
                stationService.findStationById(sourceStationId),
                stationService.findStationById(targetStationId));
        return PathResponse.of(getPathStationResponses(path), path.getDistance());
    }

    private List<PathStationResponse> getPathStationResponses(final Path path) {
        return path.getStations()
                .stream()
                .map(PathStationResponse::of)
                .collect(Collectors.toList());
    }
}
