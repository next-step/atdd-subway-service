package nextstep.subway.line.application;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Navigation;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findStationPath(final PathRequest pathRequest) {
        final Station sourceStation = stationRepository.findById(pathRequest.getSource())
            .orElseThrow(EntityNotFoundException::new);
        final Station targetStation = stationRepository.findById(pathRequest.getTarget())
            .orElseThrow(EntityNotFoundException::new);

        final List<Line> lines = lineRepository.findAll();
        Navigation navigation = Navigation.of(lines);
        return navigation.findFastPath(sourceStation, targetStation);
    }
}
