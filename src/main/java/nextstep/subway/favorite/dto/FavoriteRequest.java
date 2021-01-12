package nextstep.subway.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class FavoriteRequest {
    @NotNull
    private final Long source;
    @NotNull
    private final Long target;
}
