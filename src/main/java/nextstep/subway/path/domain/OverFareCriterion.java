package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.constants.ErrorMessages;

public enum OverFareCriterion {
    DISTANCE_NOT_OVER(0, 1),
    DISTANCE_OVER_10(10, 5),
    DISTANCE_OVER_50(50, 8);

    private int pathDistance; // 거리
    private int criterionDistance; // 요금 추가 기준 거리

    OverFareCriterion(int pathDistance, int criterionDistance) {
        this.pathDistance = pathDistance;
        this.criterionDistance = criterionDistance;
    }

    public static OverFareCriterion checkDistanceOver(int pathDistance) {
        List<OverFareCriterion> list = Arrays.stream(OverFareCriterion.values()).collect(Collectors.toList());
        return list.stream()
                .sorted(Comparator.reverseOrder())
                .filter(overFareCriterion -> pathDistance > overFareCriterion.pathDistance)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.CANNOT_CHECK_OVER_DISTANCE));
    }

    public int getPathDistance() {
        return pathDistance;
    }

    public int getCriterionDistance() {
        return criterionDistance;
    }
}
