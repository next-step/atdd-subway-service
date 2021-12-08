package nextstep.subway.station.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationService {
    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Transactional
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findStations() {
        return StationResponse.ofList(stationRepository.findAll());
    }
}
