package nextstep.subway.station.domain;

public class StationId {
    private final long stationId;

    private StationId(long stationId) {
        this.stationId = stationId;
    }

    public static StationId from(long stationId) {
        return new StationId(stationId);
    }

    public static StationId invalidId() {
        return new StationId(-1L);
    }

    public Long getLong() {
        return stationId;
    }

    public String getString() {
        return Long.toString(stationId);
    }
}
