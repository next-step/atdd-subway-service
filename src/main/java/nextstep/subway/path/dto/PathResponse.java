package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {

  private final List<StationResponse> stations;
  private final Double distance;

  public PathResponse(List<StationResponse> stations, Double distance) {
    this.stations = stations;
    this.distance = distance;
  }

  public static PathResponse from(Path shortestPath) {
    List<StationResponse> stationResponses = shortestPath.getStations()
                                            .stream()
                                            .map(StationResponse::of)
                                            .collect(Collectors.toList());
    return new PathResponse(stationResponses, shortestPath.getDistance());
  }

  public List<StationResponse> getStations() {
    return stations;
  }

  public Double getDistance() {
    return distance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PathResponse that = (PathResponse) o;
    return Objects.equals(stations, that.stations) && Objects.equals(distance, that.distance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stations, distance);
  }
}
