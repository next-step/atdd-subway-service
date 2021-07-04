package nextstep.subway.fare.policy.path;

import nextstep.subway.fare.policy.더부모인터페이스;
import nextstep.subway.path.domain.Path;

public abstract class PathAdditionalPolicy implements 더부모인터페이스 {
    protected void checkPathObject(Object object) {
        if (!(object instanceof Path)) {
            throw new IllegalArgumentException("정책 반영 기준은 이동경로여야 합니다.");
        }
    }
}
