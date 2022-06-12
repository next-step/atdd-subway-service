package nextstep.subway.path.dto;

import nextstep.subway.path.exception.StationsSameException;

public class PathRequest {

    private Long source;
    private Long target;

    public PathRequest() {
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void checkValid() {
        if (source == target) {
            throw new StationsSameException();
        }
    }

}
