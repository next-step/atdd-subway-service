package nextstep.subway.fare.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathStation;

import java.util.List;

public class FareRequest {
    private final List<PathStation> pathStations;
    private final List<Line> lines;
    private final int distance;
    private final int age;

    public FareRequest(List<PathStation> pathStations, List<Line> lines, int distance, int age) {
        this.pathStations = pathStations;
        this.lines = lines;
        this.distance = distance;
        this.age = age;
    }

    public List<PathStation> getPathStations() {
        return pathStations;
    }

    public List<Line> getLines() {
        return lines;
    }

    public int getDistance() {
        return distance;
    }

    public int getAge() {
        return age;
    }

    public boolean hasAge() {
        return age > 0;
    }
}
