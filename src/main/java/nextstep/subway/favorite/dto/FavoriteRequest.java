package nextstep.subway.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName : nextstep.subway.favorites.dto
 * fileName : FavoriteRequest
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FavoriteRequest {
    private Long source;
    private Long target;

    public static FavoriteRequest of(Long source, Long target) {
        return new FavoriteRequest(source, target);
    }
}
