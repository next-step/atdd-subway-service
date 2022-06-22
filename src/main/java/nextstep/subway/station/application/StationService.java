package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        final List<Station> stations = stationRepository.findAll();

        return Collections.unmodifiableList(stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()));
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Station findStationById(final Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Station findById(final Long id) {
        return stationRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
