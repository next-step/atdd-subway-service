package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoriteRequest {

    private Long source;
    private Long target;

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Favorite toFavorite(Member member, Station source, Station target) {
        return Favorite.of(member, source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
