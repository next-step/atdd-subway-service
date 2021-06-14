package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {
  private final List<Station> stations;
  private final Integer distance;

  public Path(List<Station> stations, Integer distance) {
    this.stations = stations;
    this.distance = distance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Path path = (Path) o;
    return Objects.equals(stations, path.stations) && Objects.equals(distance, path.distance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stations, distance);
  }
}
