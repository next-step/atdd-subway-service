package nextstep.subway.path.dto;

public class PathRequest {
    private final Long sourceStation;
    private final Long targetStation;

    public PathRequest(Long sourceStation, Long targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getSourceStation() {
        return sourceStation;
    }

    public Long getTargetStation() {
        return targetStation;
    }
}
