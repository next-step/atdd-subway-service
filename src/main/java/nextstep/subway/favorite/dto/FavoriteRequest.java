package nextstep.subway.favorite.dto;

import java.util.Objects;

public class FavoriteRequest {
    private final Long source;
    private final Long target;

    public FavoriteRequest(final Long source, final Long target) {
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(final Long source, final Long target) {
        return new FavoriteRequest(source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "FavoriteRequest{" +
                "source=" + source +
                ", target=" + target +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final FavoriteRequest that = (FavoriteRequest) o;
        return Objects.equals(source, that.source) && Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
