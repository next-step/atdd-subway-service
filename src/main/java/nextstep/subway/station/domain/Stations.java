package nextstep.subway.station.domain;

import static java.util.stream.Collectors.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Stations extends LinkedList<Station> {
    private Map<Long, Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations.stream().collect(toMap(Station::getId, Function.identity()));
    }

    public Station getStation(Long id) {
        return stations.get(id);
    }
}
