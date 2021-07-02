package nextstep.subway.favorite.dto;

import nextstep.subway.exception.CustomException;
import nextstep.subway.favorite.domain.Favorite;

import java.util.Objects;

import static nextstep.subway.exception.CustomExceptionMessage.INVALID_PARAMS;

public class FavoriteResponse {
    private Long id;
    private Long source;
    private Long target;

    public FavoriteResponse() {
        // empty
    }

    private FavoriteResponse(final Long id, final Long source, final Long target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(final Favorite favorite) {
        if (Objects.isNull(favorite)) {
            throw new CustomException(INVALID_PARAMS);
        }
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public Long getId() {
        return id;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
