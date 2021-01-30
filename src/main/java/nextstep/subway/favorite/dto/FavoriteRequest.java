package nextstep.subway.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class FavoriteRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }
}
