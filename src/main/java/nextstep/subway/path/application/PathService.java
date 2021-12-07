package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final PathFactory pathFactory;

    public PathService(LineRepository lineRepository, PathFactory pathSearch) {
        this.lineRepository = lineRepository;
        this.pathFactory = pathSearch;
    }


    public PathResponse getShortestPath(Long source, Long target) {
        Path path = new Path(lineRepository.findAll(), source, target);

        PathResult pathSearchResult = this.pathFactory.findShortestPath(path.toPathEdges(),
            path.getSource(), path.getTarget());

        List<Station> stations = path.getStationsBy(pathSearchResult.getStationIds());

        return PathResponse.of(StationResponse.toList(stations), pathSearchResult.getDistance());
    }
}
