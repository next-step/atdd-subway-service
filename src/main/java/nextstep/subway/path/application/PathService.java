package nextstep.subway.path.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(int age, PathRequest pathRequest) {
        Station source = findStationById(pathRequest.getSource());
        Station target = findStationById(pathRequest.getTarget());
        List<Section> sections = sectionRepository.findAll();

        Path path = new PathFinder(sections).getShortestPath(source, target);
        Lines lines = Sections.of(sections).findLinesContainedStations(path.getStations());
        path.calculateFare(age, lines.findMaxFare());

        return PathResponse.of(path);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException(StationExceptionCode.NOT_FOUND_BY_ID.getMessage()));
    }
}
