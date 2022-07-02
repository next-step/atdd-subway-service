package nextstep.subway.favorites.dto;

import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

public class FavoritesRequest {

    private Long sourceId;
    private Long targetId;

    public FavoritesRequest() {
    }

    public FavoritesRequest(Long sourceId, Long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Favorites toEntity(Member member, Station sourceStation, Station targetStation) {
        return new Favorites(member, sourceStation, targetStation);
    }
}
