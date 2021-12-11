package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;

    public PathService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        Optional<Station> sourceStation = stationRepository.findById(source);
        Optional<Station> targetStation = stationRepository.findById(target);

        return null;
    }
}
