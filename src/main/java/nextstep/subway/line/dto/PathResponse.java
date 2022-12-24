package nextstep.subway.line.dto;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private int totalFee;
    private int totalDistance;
    List<StationResponse> stationResponseList;

    public PathResponse() {}

    public PathResponse(int totalFee, int totalDistance, List<StationResponse> stationResponseList) {
        this.totalFee = totalFee;
        this.totalDistance = totalDistance;
        this.stationResponseList = stationResponseList;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public List<StationResponse> getStationResponseList() {
        return stationResponseList;
    }

    public static PathResponse of(int totalFee, int totalDistance, List<StationResponse> stationResponseList) {
        return new PathResponse(totalFee, totalDistance, stationResponseList);
    }
}
