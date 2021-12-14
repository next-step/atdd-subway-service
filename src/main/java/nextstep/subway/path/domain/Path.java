package nextstep.subway.path.domain;

import java.util.List;
import java.util.Objects;
import nextstep.subway.station.domain.Station;

public class Path {

    private final List<Station> path;
    private final Double pathWeight;

    public Path(List<Station> path, Double pathWeight) {
        validate(path, pathWeight);
        this.path = path;
        this.pathWeight = pathWeight;
    }

    public List<Station> getPath() {
        return path;
    }

    public Double getPathWeight() {
        return pathWeight;
    }

    public void validate(List<Station> path, Double pathWeight) {
        if (Objects.isNull(path) || path.isEmpty()) {
            throw new IllegalArgumentException("path is empty");
        }
        if (pathWeight <= 0) {
            throw new IllegalArgumentException("Not valid pathWeight. " + pathWeight);
        }
    }

}
