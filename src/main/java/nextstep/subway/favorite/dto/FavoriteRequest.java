package nextstep.subway.favorite.dto;

import nextstep.subway.common.exception.NoRequiredValueException;

import java.util.Objects;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    private FavoriteRequest(final Long source, final Long target) {
        validate(source, target);
        this.source = source;
        this.target = target;
    }

    public static FavoriteRequest of(final Long source, final Long target) {
        return new FavoriteRequest(source, target);
    }

    private void validate(final Long source, final Long target) {
        if(Objects.isNull(source) || Objects.isNull(target)) {
            throw new NoRequiredValueException();
        }
    }

    public Long getSource() {
        return this.source;
    }

    public Long getTarget() {
        return this.target;
    }
}
