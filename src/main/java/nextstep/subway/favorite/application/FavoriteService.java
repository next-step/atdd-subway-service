package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberQueryService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final MemberQueryService memberQueryService;
    private final StationService stationService;

    public FavoriteService(MemberQueryService memberQueryService,
                           StationService stationService) {
        this.memberQueryService = memberQueryService;
        this.stationService = stationService;
    }

    @Transactional
    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = findMember(loginMember);
        return toFavorite(member, request);
    }

    @Transactional
    public void removeFavorite(LoginMember loginMember, long favoriteId) {
        Member member = findMember(loginMember);
        member.removeFavorite(favoriteId);
    }

    public List<FavoriteResponse> getFavorite(LoginMember loginMember) {
        return FavoriteResponse.ofList(
                memberQueryService.findMemberById(loginMember.getId()).getFavorites());
    }

    public Favorite toFavorite(Member member, FavoriteRequest request) {
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());

        return new Favorite(member, sourceStation, targetStation);
    }

    private Member findMember(LoginMember loginMember) {
        return memberQueryService.findMemberById(loginMember.getId());
    }
}
