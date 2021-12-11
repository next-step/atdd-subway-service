package nextstep.subway.station.application;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Station> getStationsByIdIn(final Long upStationId, final Long downStationId) {
        final List<Station> stations = stationRepository.findAllById(
            Arrays.asList(upStationId, downStationId)
        );
        if (stations.size() != 2) {
            throw new NoSuchElementException();
        }
        return stations;
    }

    @Transactional(readOnly = true)
    public Station getStationById(final Long id) {
        return stationRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
