package nextstep.subway.path.application;

import java.util.List;

import nextstep.subway.line.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;


@Service
@Transactional(readOnly = true)
public class PathService {
    private StationRepository stationRepository;

    public PathService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationResponse> findPath(PathRequest pathRequest) {
        Station source = stationRepository.findById(pathRequest.getSource()).orElseThrow(() -> new RuntimeException(Sections.NOT_FOUND_SECTION));
        Station target = stationRepository.findById(pathRequest.getTarget()).orElseThrow(() -> new RuntimeException(Sections.NOT_FOUND_SECTION));
        return Path.findShortestPath(source, target);
    }
}
