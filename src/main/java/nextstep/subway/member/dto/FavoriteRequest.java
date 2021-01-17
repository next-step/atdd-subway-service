package nextstep.subway.member.dto;

import java.util.Arrays;
import java.util.List;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public List<Long> toStationIds() {
        return Arrays.asList(source, target);
    }
}
