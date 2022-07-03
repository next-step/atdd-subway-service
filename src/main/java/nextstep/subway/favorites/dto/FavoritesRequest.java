package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoritesRequest {

    private Long source;
    private Long target;

    public FavoritesRequest() {
    }

    public FavoritesRequest(Long source, Long target) {
        this.source = source;
        this.target = target;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public Favorites toEntity(Member member, Station sourceStation, Station targetStation) {
        return new Favorites(member, sourceStation, targetStation);
    }
}
