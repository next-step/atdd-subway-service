package nextstep.subway.path.domain;

import java.util.Objects;

public class PathDistance {

    private int pathDistance;

    public PathDistance(int pathDistance) {
        this.pathDistance = pathDistance;
    }

    public int getPathDistance() {
        return pathDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathDistance that = (PathDistance) o;
        return pathDistance == that.pathDistance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathDistance);
    }
}
