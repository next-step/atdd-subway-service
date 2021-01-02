package nextstep.subway.station.application;

import nextstep.subway.exceptions.EntityNotFoundException;
import nextstep.subway.station.application.exceptions.StationEntityNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        try {
            stationRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("존재하지 않는 역입니다.");
        }

    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new StationEntityNotFoundException("존재하지 않는 역입니다."));
    }

    public List<Station> findAllStationsByIds(List<Long> stationIds) {
        return stationRepository.findAllById(stationIds);
    }

    @Transactional(readOnly = true)
    public boolean isExistStation(Long stationId) {
        return stationRepository.existsById(stationId);
    }
}
