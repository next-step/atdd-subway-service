package nextstep.subway.path.dto;

public class PathRequest {

    private long sourceId;
    private long targetId;

    private PathRequest() {
    }

    public PathRequest(long sourceId, long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }
}
