package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteResponse {
    private Long id;
    private Station source;
    private Station target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Station source, Station target) {
        this.id = id;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget());
    }

    public static List<FavoriteResponse> listOf(List<Favorite> members) {
        return members.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
