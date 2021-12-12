package nextstep.subway.station.application;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.station.domain.*;
import nextstep.subway.station.dto.*;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
