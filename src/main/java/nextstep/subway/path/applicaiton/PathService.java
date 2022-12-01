package nextstep.subway.path.applicaiton;

import nextstep.subway.line.domain.SectionRepository;
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
        PathFinder pathFinder = PathFinder.of(stationRepository.findAll(), sectionRepository.findAll());
        return PathResponse.from(pathFinder.findPath(findStationById(sourceId), findStationById(targetId)));
    }


    private Station findStationById(Long sourceId) {
        return stationRepository.findById(sourceId).orElseThrow(() -> new IllegalArgumentException("등록되지 않은 역입니다."));
    }

}
