package nextstep.subway.path.domain;

import java.util.Objects;

public class PathInfo {

    private PathDistance pathDistance;
    private Fare fare;

    public PathInfo() {
    }

    public PathInfo(PathDistance pathDistance, Fare fare) {
        this.pathDistance = pathDistance;
        this.fare = fare;
    }

    public PathDistance getPathDistance() {
        return pathDistance;
    }

    public int getPathDistanceValue() {
        return pathDistance.getPathDistance();
    }

    public Fare getFare() {
        return fare;
    }

    public Double getFareValue() {
        return fare.getFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathInfo pathInfo = (PathInfo) o;
        return Objects.equals(pathDistance, pathInfo.pathDistance) && Objects.equals(fare,
                pathInfo.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathDistance, fare);
    }
}
