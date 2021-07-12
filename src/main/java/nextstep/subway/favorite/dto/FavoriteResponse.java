package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteResponse {
    private Long id;
    private Long memberId;
    private Long source;
    private Long target;

    public FavoriteResponse() {
    }

    public FavoriteResponse(Long id, Long memberId, Long source, Long target) {
        this.id = id;
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getMemberId(),favorite.gettargetStationId(), favorite.getSourceStationId());
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }
}
