package nextstep.subway.station.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.station.domain.*;
import nextstep.subway.station.dto.*;

@Service
@Transactional(readOnly = true)
public class StationReadService {
    private final StationRepository stationRepository;

    public StationReadService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}
