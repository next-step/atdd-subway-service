package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite);
    }

    public static List<FavoriteResponse> ofList(List<Favorite> findAll) {
        return findAll.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    public FavoriteResponse(Favorite favorite) {
        this.id = favorite.getId();
        this.source = StationResponse.of(favorite.getStartStation());
        this.target = StationResponse.of(favorite.getEndStation());
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
