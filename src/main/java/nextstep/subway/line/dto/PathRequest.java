package nextstep.subway.line.dto;

import nextstep.subway.exception.IsEqualsTwoStationsException;

public class PathRequest {
    private Long source;
    private Long target;

    private PathRequest() {
    }

    public PathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void isEqualsStations() {
        if (source.equals(target)) {
            throw new IsEqualsTwoStationsException();
        }
    }
}
