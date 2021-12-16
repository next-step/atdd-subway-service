package nextstep.subway.path.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
  private final List<StationResponse> stations;
  private final int distance;
  private final int totalFare;

  public PathResponse(List<StationResponse> stations, int distance, int totalFare) {
    this.stations = stations;
    this.distance = distance;
    this.totalFare = totalFare;
  }

  public static PathResponse of(List<Station> stations, int distance, int totalFare) {
    return new PathResponse(StationResponse.ofList(stations), distance, totalFare);
  }

  public List<StationResponse> getStations() {
    return stations;
  }

  public int getDistance() {
    return distance;
  }

  public int getTotalFare() {
    return totalFare;
  }
}
