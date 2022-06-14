package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.application.AuthorizationException;
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
@Transactional(readOnly = true)
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

    public List<FavoriteResponse> findFavorite(Long loginMemberId) {
        List<Favorite> favorites = this.favoriteRepository.findAllByMemberId(loginMemberId);

        return favorites.stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = this.memberService.findMemberById(loginMember.getId());
        Station sourceStation = this.stationService.findStationById(favoriteRequest.getSource());
        Station targetStation = this.stationService.findStationById(favoriteRequest.getTarget());

        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        favoriteRepository.save(favorite);

        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = this.findFavoriteById(favoriteId);
        if (!favorite.isOwner(loginMember.getId())) {
            throw new AuthorizationException("즐겨찾기 등록자가 아닙니다.");
        }

        this.favoriteRepository.delete(favorite);
    }

    public Favorite findFavoriteById(Long id) {
        return this.favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

}
