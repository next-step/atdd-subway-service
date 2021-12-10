package nextstep.subway.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName : nextstep.subway.favorites.dto
 * fileName : FavoriteResponse
 * author : haedoang
 * date : 2021-12-07
 * description :
 */
@Getter
@NoArgsConstructor
public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSourceStation(), favorite.getTargetStation());
    }

    public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
