package nextstep.subway.path.domain;

import nextstep.subway.line.domain.vo.Amount;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

public class Path {
  private final List<Station> stations;
  private final Double distance;
  private final Amount pathAdditionalAmount;

  public Path(List<Station> stations, Double distance, Amount pathAdditionalAmount) {
    this.stations = stations;
    this.distance = distance;
    this.pathAdditionalAmount = pathAdditionalAmount;
  }

  public List<Station> getStations() {
    return stations;
  }

  public Double getDistance() {
    return distance;
  }

  public Long getAdditionalAmount() {
    return pathAdditionalAmount.getAmount();
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
