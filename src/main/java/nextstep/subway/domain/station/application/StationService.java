package nextstep.subway.domain.station.application;

import nextstep.subway.domain.station.domain.Station;
import nextstep.subway.domain.station.domain.StationRepository;
import nextstep.subway.domain.station.dto.StationRequest;
import nextstep.subway.domain.station.dto.StationResponse;
import nextstep.subway.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Station findById(Long id) {
        return stationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
