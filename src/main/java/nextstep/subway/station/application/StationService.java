package nextstep.subway.station.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.station.application.exception.StationErrorCode;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.infrastructure.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());

        return StationResponse.of(persistStation);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return StationResponse.toList(stations);
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> NotFoundException.of(StationErrorCode.SECTION_NOT_FOUND));
    }

    public void validStationExist(Long stationId) {
        boolean exist = stationRepository.existsById(stationId);
        if (!exist) {
            throw NotFoundException.of(StationErrorCode.SECTION_NOT_FOUND);
        }
    }

    public List<Station> findAllById(List<Long> stationIds) {
        return stationRepository.findAllById(stationIds);
    }
}
