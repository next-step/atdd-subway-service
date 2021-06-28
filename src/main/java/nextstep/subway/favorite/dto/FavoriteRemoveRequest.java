package nextstep.subway.favorite.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FavoriteRemoveRequest {
    public static final long FAVORITE_ID_MIN_VALUE = 1L;
    public static final String INVALID_FAVORITE_ID = "올바른 역 번호를 넣어주세요.";

    @Min(value = FAVORITE_ID_MIN_VALUE, message = INVALID_FAVORITE_ID)
    @NotNull(message = INVALID_FAVORITE_ID)
    private Long id;

    public FavoriteRemoveRequest() {
    }

    public FavoriteRemoveRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
