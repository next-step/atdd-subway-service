package nextstep.subway.favorites.dto;

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
    private String source;
    private String target;

    public static FavoriteRequest of(String source, String target) {
        return new FavoriteRequest(source, target);
    }
}
