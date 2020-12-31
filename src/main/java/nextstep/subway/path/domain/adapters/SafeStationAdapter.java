package nextstep.subway.path.domain.adapters;

import nextstep.subway.path.domain.SafeStationInfo;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SafeStationAdapter implements SafeStation {
    private final StationService stationService;

    public SafeStationAdapter(StationService stationService) {
        this.stationService = stationService;
    }

    @Override
    public List<SafeStationInfo> findStationsById(final List<Long> stationIds) {
        List<Station> allStations = stationService.findAllStationsByIds(stationIds);

        return allStations.stream()
                .map(it -> new SafeStationInfo(it.getId(), it.getName(), it.getCreatedDate()))
                .collect(Collectors.toList());
    }
}
