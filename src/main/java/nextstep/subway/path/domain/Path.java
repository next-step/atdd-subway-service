package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Line> transferLines;
    private final List<Station> stations;
    private final double distance;

    public Path(List<Station> stations, double distance, List<Line> transferLines) {
        this.stations = stations;
        this.distance = distance;
        this.transferLines = transferLines;
    }

    public static Path of(List<Station> stations, double distance, List<Line> transferLine) {
        return new Path(stations, distance, transferLine);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getTransferLines() {
        return transferLines;
    }

    public double getDistance() {
        return distance;
    }
}
