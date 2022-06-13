package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, long sourceId, long targetId) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station source = stationService.findById(sourceId);
        Station target = stationService.findById(targetId);
        Favorite persistFavorite = favoriteRepository.save(Favorite.builder(member, source, target)
                .build());
        FavoriteResponse favoriteResponse = FavoriteResponse.of(persistFavorite);
        return favoriteResponse;
    }
}
