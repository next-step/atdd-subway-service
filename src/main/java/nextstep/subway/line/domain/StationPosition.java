package nextstep.subway.line.domain;

public enum StationPosition {
    UP_STATION,
    DOWN_STATION;

    public boolean isUpStation() {
        return this == UP_STATION;
    }

    public boolean isDownStation() {
        return this == DOWN_STATION;
    }

    public boolean isOpposite(StationPosition stationPosition) {
        return stationPosition != null && this != stationPosition;
    }
}
