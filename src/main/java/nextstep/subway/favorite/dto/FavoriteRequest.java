package nextstep.subway.favorite.dto;

import nextstep.subway.utils.StringUtils;

public class FavoriteRequest {

    private String source;
    private String target;

    private FavoriteRequest() {
    }

    public FavoriteRequest(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Long findSourceId() {
        return StringUtils.StringToLong(source);
    }

    public Long findTargetId() {
        return StringUtils.StringToLong(target);
    }
}
