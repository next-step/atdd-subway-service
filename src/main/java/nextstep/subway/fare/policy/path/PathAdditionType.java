package nextstep.subway.fare.policy.path;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PathAdditionType {
    LINE(new LineAdditionalPolicy()),
    DISTANCE(new DistanceAdditionalPolicy());

    PathAdditionalPolicy policy;

    PathAdditionType(PathAdditionalPolicy policy) {
        this.policy = policy;
    }

    public static List<PathAdditionalPolicy> getAllPolicies() {
        return Arrays.stream(values())
            .map(p -> p.policy)
            .collect(Collectors.toList());
    }
}
