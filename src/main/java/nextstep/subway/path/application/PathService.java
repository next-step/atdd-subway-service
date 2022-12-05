package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public PathService(StationService stationService, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(LoginMember loginMember, PathRequest pathRequest) {
        Station source = stationService.stationById(pathRequest.getSource());
        Station target = stationService.stationById(pathRequest.getTarget());
        List<Section> sections = sectionRepository.findAll();

        Path path = new StationGraph(sections).findShortestPath(source, target);
        List<Line> lines = Sections.of(sections).findLinesContainedStations(path.getStations());
        path.calculateFare(loginMember, lines);

        return PathResponse.of(path);
    }
}
