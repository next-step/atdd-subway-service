package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private LineRepository lineRepository;
    private StationService stationService;
    private SectionRepository sectionRepository;

    public PathService(StationService stationService, LineRepository lineRepository, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse getShortestPath(Long source, Long target, Integer age) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        List<Section> sections = sectionRepository.findAll();

        PathFinder pathFinder = new PathFinder(lines);
        Path path = pathFinder.getShortestPath(sourceStation, targetStation);
        Fare fare = Fare.of(sections, path, age);

        return PathResponse.of(path, fare.getFare());
    }
}
