package nextstep.subway.path.application;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.path.domain.PathSelector;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;

    public PathService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 달라야 합니다");
        }

        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(NotFoundStationException::new);
        Station targetStation = stationRepository.findById(target)
                .orElseThrow(NotFoundStationException::new);

        return PathResponse.of(PathSelector.select(sourceStation, targetStation));
    }
}
