package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.FareDiscounter;
import nextstep.subway.path.domain.PathFare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathMap;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PathService {
    private final PathFinder pathFinder;
    private final PathMap pathMap;
    private final PathFare pathFare;
    private final FareDiscounter fareDiscounter;
    private final StationService stationService;
    private final LineService lineService;
    private final SectionRepository sectionRepository;

    @Autowired
    public PathService(PathFinder pathFinder, PathMap pathMap, PathFare pathFare, FareDiscounter fareDiscounter,
                       StationService stationService, LineService lineService, SectionRepository sectionRepository) {
        this.pathFinder = pathFinder;
        this.pathMap = pathMap;
        this.pathFare = pathFare;
        this.fareDiscounter = fareDiscounter;
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        WeightedMultigraph<Station, DefaultWeightedEdge> map = pathMap.createMap(lineService.findAll());
        Map<String, Object> data = pathFinder.findShortestPath(map, source, target);
        int fare = pathFare.calculateFare((List<DefaultWeightedEdge>) data.get("edge"), sectionRepository.findAll());
        fare = fareDiscounter.discountFare(fare, loginMember.isGuest() ? 20 : loginMember.getAge());

        return new PathResponse((List<Station>) data.get("vertex"), (double) data.get("weight"), fare);
    }
}
