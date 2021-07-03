package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional(readOnly = true)
    public Station getStationById(long id) {
        return stationRepository.findById(id).orElseThrow(() -> new StationNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Lines findLines() {
        return new Lines(lineRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PathResponse searchPath(final PathRequest request) {
        Path<Station> path = getPath(request);

        return PathResponse.builder()
                .stationList(path.getPaths())
                .distance(path.getDistance())
                .charges(path.getCharges())
                .build();
    }

    private Path<Station> getPath(final PathRequest request) {
        Station source = getStationById(request.getSource());
        Station target = getStationById(request.getTarget());

        return new PathFinder(findLines()).getPath(source, target);
    }
}
