package nextstep.subway.line.dto.path;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathResult {

    private final List<Station> result;
    private final int weight;

    public PathResult(List<Station> result, int weight) {
        this.result = result;
        this.weight = weight;
    }

    public static PathResult of(List<Station> result, int weight) {
        return new PathResult(result, weight);
    }

    public List<Station> getResult() {
        return result;
    }

    public int getWeight() {
        return weight;
    }
}
