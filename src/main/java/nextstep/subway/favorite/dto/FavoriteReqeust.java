package nextstep.subway.favorite.dto;

public class FavoriteReqeust {
    private Long source;
    private Long target;

    public FavoriteReqeust(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Long getTarget() {
        return target;
    }

    public void setTarget(Long target) {
        this.target = target;
    }
}
