package nextstep.subway.favorite.application;

import java.util.List;
import javax.transaction.Transactional;
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

@Transactional
@Service
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public Favorite createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberOrThrow(loginMember.getId());
        Station sourceStation = stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = stationService.findStationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(member.getId(), sourceStation, targetStation));
        return favorite;
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberOrThrow(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return FavoriteResponse.of(favorites);
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberService.findMemberOrThrow(loginMember.getId());
        favoriteRepository.deleteByMemberIdAndId(member.getId(), id);
    }
}
