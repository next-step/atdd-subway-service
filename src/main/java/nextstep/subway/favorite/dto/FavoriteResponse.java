package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                StationResponse.of(favorite.getSourceStation()),
                StationResponse.of(favorite.getTargetStation()));
    }

    public static List<FavoriteResponse> ofList(List<Favorite> favorites) {
        List<FavoriteResponse> favoriteResponses = new ArrayList<>();
        for (Favorite favorite : favorites) {
            favoriteResponses.add(FavoriteResponse.of(favorite));
        }
        return favoriteResponses;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
