package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    public StationResponses findAllStations() {
        List<Station> stations = stationRepository.findAll();
        return new StationResponses(stations);
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    public Station findById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

}
