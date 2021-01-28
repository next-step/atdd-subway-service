package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.util.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse selectShortPath(Long source, Long target) {
        Station startStation = stationFindById(source);
        Station arrivalStation = stationFindById(target);

        List<Line> lines = lineRepository.findAll();

        Path path = Path.of(linesFlatMapSections(lines));
        return PathResponse.of(path.selectShortestPath(startStation, arrivalStation), path.selectPathDistance());
    }

    private List<Section> linesFlatMapSections(List<Line> lines) {
        return lines.stream()
                .flatMap(line -> line.getAllSection().stream())
                .collect(toList());
    }

    private Station stationFindById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Message.NOT_EXIST_STATION_MESSAGE));
    }
}
