package nextstep.subway.path.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

  private final List<StationResponse> stations;
  private final Integer distance;

  public PathResponse(List<StationResponse> stations, Integer distance) {
    this.stations = stations;
    this.distance = distance;
  }

  public List<StationResponse> getStations() {
    return stations;
  }

  public Integer getDistance() {
    return distance;
  }
}
