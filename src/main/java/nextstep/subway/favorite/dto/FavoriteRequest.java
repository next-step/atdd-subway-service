package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteRequest {
    private Long source;
    private Long target;

    public FavoriteRequest() {}

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Favorite toFavorite(Long memberId, Station source, Station target){
        return new Favorite(memberId, source, target);
    }
}
