package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathGraph;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final PathGraph pathGraph;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.pathGraph = PathGraph.createJgraphPathGraph();
    }

    public PathResponse findShortPath(LoginMember loginMember, PathRequest pathRequest) {
        List<Section> sections = sectionRepository.findAll();
        Station source = stationRepository.getById(pathRequest.getSource());
        Station target = stationRepository.getById(pathRequest.getTarget());

        final Path shortestPath = pathGraph.findShortestPath(sections, source, target);
        final Fare calcFare = shortestPath.getFare();

        return new PathResponse(shortestPath, calcFare.calculateAgeFare(loginMember.getAgeType()));
    }

}
