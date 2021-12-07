package nextstep.subway.path.dto;

import java.util.List;
import nextstep.subway.station.domain.Station;

public class PathResultV2 {

    private final List<Station> result;
    private final int weight;

    public PathResultV2(List<Station> result, int weight) {
        this.result = result;
        this.weight = weight;
    }

    public static PathResultV2 of(List<Station> result, int weight) {
        return new PathResultV2(result, weight);
    }

    public List<Station> getResult() {
        return result;
    }

    public int getWeight() {
        return weight;
    }
}
