package nextstep.subway.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;
}
