package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class Stations {
  List<Station> stations = new ArrayList<>();

  public Stations(List<Station> stations) {
    this.stations.addAll(stations);
  }

  public boolean containsAny(List<Station> stationList) {
    return stationList.removeAll(stations);
  }

  public boolean contains(Station station) {
    return stations.contains(station);
  }

  public List<Station> getStations() {
    return stations;
  }
}
