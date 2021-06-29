package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.domain.policy.FarePolicy;
import nextstep.subway.path.domain.policy.distance.DistancePolicyFactory;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
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
        PathFinder pathFinder = new StationPathFinder(
                new DijkstraShortestPath(new SubwayMapData(lines, new WeightedMultigraph(SectionEdge.class)).initData()),
                new Direction(source, target));
        PathResult paths = pathFinder.findPaths();
        Fare fare = new Fare();
        FarePolicy distancePolicy = DistancePolicyFactory.findPolicy(paths.getTotalDistance());
        return PathResponse.of(paths, distancePolicy.calculate(fare.getFareValue()));
    }

    private Lines findLines() {
        return new Lines(lineRepository.findAll());
    }
}
