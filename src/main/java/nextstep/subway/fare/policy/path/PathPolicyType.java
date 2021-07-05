package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.path.domain.Path;

public enum PathPolicyType {
    LINE(new LineAdditionalFarePolicy()),
    DISTANCE(new DistanceAdditionalFarePolicy());

    PathAdditionalFarePolicy policy;

    PathPolicyType(PathAdditionalFarePolicy policy) {
        this.policy = policy;
    }

    public FarePolicy getPolicy(Path path) {
        return policy.getPolicy(path);
    }
}
