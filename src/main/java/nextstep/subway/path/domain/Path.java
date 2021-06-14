package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;

public class Path {
  private final List<Long> stationIds;
  private final Integer distance;

  public Path(List<Long> stationIds, Integer distance) {
    this.stationIds = stationIds;
    this.distance = distance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Path path = (Path) o;
    return Objects.equals(stationIds, path.stationIds) && Objects.equals(distance, path.distance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stationIds, distance);
  }
}
