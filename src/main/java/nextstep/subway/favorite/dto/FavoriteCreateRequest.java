package nextstep.subway.favorite.dto;

public class FavoriteCreateRequest {

    private final Long source;
    private final Long target;

    public FavoriteCreateRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
