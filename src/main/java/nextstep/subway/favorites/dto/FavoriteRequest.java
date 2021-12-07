package nextstep.subway.favorites.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.station.domain.Station;
import org.aspectj.apache.bcel.generic.LineNumberGen;

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
    private Long sourceStationId;
    private Long targetStationId;

    public static FavoriteRequest of(Long sourceStationId, Long targetStationId) {
        return new FavoriteRequest(sourceStationId, targetStationId);
    }

}
