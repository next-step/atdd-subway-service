package nextstep.subway.favorite.dto;

public class FavoriteRequest {

    final private Long source;
    final private Long target;

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
}
