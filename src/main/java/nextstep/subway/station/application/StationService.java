package nextstep.subway.station.application;

import java.util.Collections;
import nextstep.subway.line.application.NotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.from(persistStation);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<StationResponse> result = stationRepository.findAll().stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    @Transactional(readOnly = true)
    public Station findStationByIdAsDomainEntity(Long id) {
        return stationRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
    }

}
