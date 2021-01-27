package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse minimumPath(PathRequest pathRequest) {
        Station startStation = stationFindById(pathRequest.getSource());
        Station arrivalStation = stationFindById(pathRequest.getSource());

        List<Line> lines = lineRepository.findAll();
        Path path = new Path(lines, startStation, arrivalStation);

        return new PathResponse();
    }

    private Station stationFindById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 지하철역입니다."));
    }
}
