package nextstep.subway.favorite.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {

    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public Long getId() {
        return id;
    }

    public StationResponse getSource() {
        return StationResponse.of(source);
    }

    public StationResponse getTarget() {
        return StationResponse.of(target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public static List<FavoriteResponse> of(List<Favorite> favorites) {

        return favorites.stream()
            .map(favorite -> FavoriteResponse.of(favorite))
            .collect(Collectors.toList());
    }
}
