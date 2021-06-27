package nextstep.subway.station.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.station.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public StationService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }

    public PathResponse findPaths(Long sourceStationId, Long targetStationId) {
        Station sourceStation = findById(sourceStationId);
        Station targetStation = findById(targetStationId);
        List<Line> persistLines = lineRepository.findAll();
        PathFinder pathFinder = new PathFinder(persistLines);
        return pathFinder.findPaths(sourceStation, targetStation);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }
}
