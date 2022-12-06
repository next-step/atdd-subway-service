package nextstep.subway.path.applicaiton;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.DijkstraPathFindStrategy;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPaths(Long sourceId, Long targetId) {
        Sections sections = Sections.from(sectionRepository.findAll());
        PathFinder pathFinder = PathFinder.of(stationRepository.findAll(), sections.getSections());
        Path path = pathFinder.findPath(new DijkstraPathFindStrategy(), findStationById(sourceId),
                findStationById(targetId));
        Lines lines = Lines.From(sections.getStationMatchedLines(path.getStations()));

        return PathResponse.from(path, Fare.of(path.getDistance(), lines));
    }


    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 역입니다."));
    }

}
