package nextstep.subway.favorite.dto;

import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;

public class FavoriteResponse {
    private Long id;
    private MemberResponse member;
    private StationResponse sourceStation;
    private StationResponse targetStation;

    public FavoriteResponse() {
    }

    public FavoriteResponse(final Long id,
                            final MemberResponse member,
                            final StationResponse sourceStation,
                            final StationResponse targetStation) {
        this.id = id;
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public MemberResponse getMember() {
        return member;
    }

    public StationResponse getSourceStation() {
        return sourceStation;
    }

    public StationResponse getTargetStation() {
        return targetStation;
    }
}
