package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Graph;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.policy.fare.FarePolicies;
import nextstep.subway.path.domain.policy.fare.FarePoliciesService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final FarePoliciesService farePoliciesService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository, FarePoliciesService farePoliciesService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.farePoliciesService = farePoliciesService;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        List<Line> lines = lineRepository.findAll();

        Graph graph = new Graph();
        Path path = graph.findShortestPath(lines, source, target);
        FarePolicies farePolicies = farePoliciesService.findStrategies(loginMember, path.getDistance());
        Fare fare = new Fare();
        fare.calculate(path, loginMember, farePolicies);

        return PathResponse.of(path, fare);
    }

    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("역이 존재하지 않습니다."));
    }
}
