package nextstep.subway.station.domain;

import nextstep.subway.station.exception.StationDuplicateException;
import nextstep.subway.station.exception.StationNotFoundException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.station.domain
 * fileName : Stations
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
public class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stationList) {
        validateDuplicate(stationList);
        this.stations = new ArrayList<>(stationList);
    }

    private void validateDuplicate(List<Station> stationList) {
        Set<String> nameSet = stationList.stream()
                .map(Station::getName)
                .collect(Collectors.toSet());
        
        if (nameSet.size() < stationList.size()) {
            throw new StationDuplicateException();
        }
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public Station getStation(long stationId) {
        return stations.stream().filter(station -> station.getId().equals(stationId))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    public void setVertex(WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        stations.stream().forEach(graph::addVertex);
    }
}
