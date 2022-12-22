package nextstep.subway.path.domain;

import java.util.Objects;

public class PathDistance {

    private static final int OVER_FARE_NOT_CHARGED_MIN_DISTANCE = 10;
    private int pathDistance;

    public PathDistance(int pathDistance) {
        this.pathDistance = pathDistance;
    }

    public int getOverFareFactor() {
        int overDistance = 0;
        OverFareCriterion overFareCriterion = OverFareCriterion.checkDistanceOver(pathDistance);
        if (overFareCriterion != OverFareCriterion.DISTANCE_NOT_OVER) {
            overDistance = pathDistance - OVER_FARE_NOT_CHARGED_MIN_DISTANCE;
        }
        int criterion = overFareCriterion.getCriterionDistance();
        return (int) (Math.ceil((overDistance - 1) / criterion) + 1);
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
