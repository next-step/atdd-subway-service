package nextstep.subway.favorite.dto;

public class FavoriteReuqest {

    private Long source;
    private Long target;

    protected FavoriteReuqest() {
    }

    public FavoriteReuqest(Long source, Long target) {
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
