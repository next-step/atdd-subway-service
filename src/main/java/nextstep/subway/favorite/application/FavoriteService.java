package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService,
                           MemberService memberService,
                           FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberById(loginMember.getId());
        Favorite favorite = toFavorite(member, request);
        return new FavoriteResponse(favoriteRepository.save(favorite));
    }

    private Favorite toFavorite(Member member, FavoriteRequest request) {
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());

        return new Favorite(member, sourceStation, targetStation);
    }

    public List<FavoriteResponse> getFavorite(LoginMember loginMember) {
        Member member = memberService.findMemberById(loginMember.getId());

        List<Favorite> favorites = member.getFavorites();

        return FavoriteResponse.ofList(favorites);
    }
}
