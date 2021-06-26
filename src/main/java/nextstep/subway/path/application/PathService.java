package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.domain.SubwayMapData;
import nextstep.subway.path.domain.SubwayNavigation;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import org.springframework.stereotype.Service;

@Service
public class PathService {

    private LineRepository lineRepository;

    public PathService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Lines lines = findLines();
        Station source = lines.findStationById(pathRequest.getSource());
        Station target = lines.findStationById(pathRequest.getTarget());
        SubwayNavigation subwayNavigation = new SubwayNavigation(new SubwayMapData(lines.getLines(), SectionEdge.class).initData());

        return PathResponse.of(subwayNavigation.getPaths(source, target),
                subwayNavigation.getDistance(source, target));
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }
}
