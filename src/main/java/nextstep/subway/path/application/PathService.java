package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathAssembler;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.StationGraph;
import nextstep.subway.path.dto.PathDtos;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.repository.SectionRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public PathService(SectionRepository sectionRepository,
        StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public PathResponse getPath(LoginMember loginMember, PathRequest pathRequest) {
        List<Section> sections = sectionRepository.findAll();
        PathDtos pathDtos = PathDtos.from(sections);

        StationGraph graph = new StationGraph(pathDtos);
        Station source = stationService.findById(pathRequest.getSource());
        Station target = stationService.findById(pathRequest.getTarget());

        PathFinder pathFinder = new PathFinder(graph, source, target);

        return PathAssembler.writeResponse(loginMember, pathFinder, sections);
    }
}
