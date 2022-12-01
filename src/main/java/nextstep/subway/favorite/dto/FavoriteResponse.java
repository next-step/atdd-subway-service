package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private StationResponse source;
    private StationResponse target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Member member, Station source, Station target) {
        this.id = member.getId();
        this.source = StationResponse.of(source);
        this.target = StationResponse.of(target);
    }

    public static FavoriteResponse of(Member member, Station source, Station target) {
        return new FavoriteResponse(member, source, target);
    }

    public static FavoriteResponse of(Favorite favorite){
        return new FavoriteResponse(favorite.getMember(),favorite.getSourceStation(),favorite.getTargetStation());
    }

    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }
}
