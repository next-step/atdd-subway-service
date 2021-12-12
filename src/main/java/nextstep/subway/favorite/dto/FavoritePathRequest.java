package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.FavoritePath;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoritePathRequest {
    private Long source;
    private Long target;

    public FavoritePathRequest() {
    }

    public FavoritePathRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public FavoritePath toFavoritePath(Member member, Station source, Station target) {
        return new FavoritePath(member, source, target);
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
