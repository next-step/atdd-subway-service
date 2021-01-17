package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse(Long id, Long memberId, Station source, Station target) {
        this.id = id;
        this.memberId = memberId;
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMember().getId(), favorite.getSource(), favorite.getTarget());
    }

    public Long getId() {
        return id;
    }
}
