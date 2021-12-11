package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
  private List<StationResponse> stations;
  private int distance;

  public PathResponse(List<StationResponse> stations, int distance) {
    this.stations = stations;
    this.distance = distance;
  }

  public static PathResponse of(List<Station> stations, int distance) {
    return new PathResponse(StationResponse.ofList(stations), distance);
  }

  public List<StationResponse> getStations() {
    return stations;
  }

  public int getDistance() {
    return distance;
  }
}
