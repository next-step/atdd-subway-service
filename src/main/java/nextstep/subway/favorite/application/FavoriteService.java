package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(StationService stationService, MemberService memberService) {
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(Long memberId, Long sourceId, Long targetId) {
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        Member member = memberService.findById(memberId);
        return FavoriteResponse.of(new Favorite(member, source, target));
    }
}
