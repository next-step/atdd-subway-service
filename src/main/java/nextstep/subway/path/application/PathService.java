package nextstep.subway.path.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(PathRequest pathRequest) {
        Station source = findStationById(pathRequest.getSource());
        Station target = findStationById(pathRequest.getTarget());

        return PathResponse.of(new PathFinder(sectionRepository.findAll()).getShortestPath(source, target));
    }

    @Transactional(readOnly = true)
    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("역이 존재하지 않습니다.", stationId));
    }
}
