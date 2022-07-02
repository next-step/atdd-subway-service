package nextstep.subway.favorite.application;

import java.util.List;
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

@Service
public class FavoriteService {
    private MemberService memberService;
    private StationService stationService;

    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService,
                           FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationService.findStationById(request.getSourceStationId());
        Station target = stationService.findStationById(request.getTargetStationId());

        Member member = memberService.findMemberById(loginMember.getId());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(source, target, member));

        return FavoriteResponse.from(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        List<Favorite> favorites = favoriteRepository.findByMember_id(memberId);
        return FavoriteResponse.from(favorites);
    }
}
