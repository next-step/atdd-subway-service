package nextstep.subway.path.dto;

import java.util.Objects;

import nextstep.subway.station.domain.Station;

public class PathAnalysisKey {
    private Long stationId;
    private String stationName;

    private PathAnalysisKey(Long stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
    }

    public static PathAnalysisKey of(Station station) {
        return new PathAnalysisKey(station.getId(), station.getName());
    }
    
    public Long getStationId() {
        return this.stationId;
    }

    public String getStationName() {
        return this.stationName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PathAnalysisKey)) {
            return false;
        }
        PathAnalysisKey pathAnalysisKey = (PathAnalysisKey) o;
        return Objects.equals(stationId, pathAnalysisKey.stationId) && Objects.equals(stationName, pathAnalysisKey.stationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, stationName);
    }
}
