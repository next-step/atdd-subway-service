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
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberRepository,
        StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberRepository;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember,
        FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Favorite persistFavorite = favoriteRepository.save(
            favoriteRequest.toFavorite(member, source, target));
        favoriteRepository.save(persistFavorite);
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return FavoriteResponse.ofList(favorites);
    }

    public void deleteFavoriteById(LoginMember loginMember, Long id) {
        Favorite favorite = favoriteRepository.findByMemberIdAndId(loginMember.getId(), id)
            .orElseThrow(FavoriteNotFoundException::new);

        favoriteRepository.delete(favorite);
    }
}
