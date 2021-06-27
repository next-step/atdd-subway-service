package nextstep.subway.path.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse findPath(long sourceId, long targetId) {
        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);

        List<Station> stations = stationRepository.findAll();
        List<Section> sections = sectionRepository.findAll();

        PathFinder pathFinder = PathFinder.of(sourceStation, targetStation, stations, sections);
        return new PathResponse(pathFinder.getStations(), pathFinder.getDistance());
    }
}
