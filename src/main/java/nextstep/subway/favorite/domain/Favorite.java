package nextstep.subway.favorite.domain;

public class Favorite {
    private Long id;
    private Long source;
    private Long target;

    protected Favorite() {
        // empty
    }

    public Favorite(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return this.id;
    }

    public Long getSource() {
        return this.source;
    }

    public Long getTarget() {
        return this.target;
    }
}
