package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {

    private Long id;
    private StationResponse source;
    private StationResponse target;

    public static List<FavoriteResponse> asList(List<Favorite> favorites) {
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public static FavoriteResponse of(Favorite favorite) {
        Station sourceStation = favorite.getSourceStation();
        Station targetStation = favorite.getTargetStation();
        return new FavoriteResponse(favorite.getId(), StationResponse.of(sourceStation), StationResponse.of(targetStation));
    }

    public FavoriteResponse(Long id, StationResponse source, StationResponse target) {
        this.id = id;
        this.source = source;
        this.target = target;
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
