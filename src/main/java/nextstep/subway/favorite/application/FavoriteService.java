package nextstep.subway.favorite.application;

import nextstep.subway.advice.exception.FavoriteBadRequestException;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final MemberService memberService;
    private final StationService stationService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final StationService stationService, final FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        Member member = memberService.getMember(id);
        List<Favorite> favorites = member.getFavorites();

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.getMember(loginMember.getId());
        Station sourceStation = stationService.findById(request.getSourceStationId());
        Station targetStation = stationService.findById(request.getTargetStationId());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        member.addFavorite(favorite);
        return FavoriteResponse.of(favorite);
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        deleteFavorite(loginMember.getId(), getFavorite(id));
    }

    public void deleteFavorite(Long memberId, Favorite favorite) {
        Member member = memberService.getMember(memberId);
        member.removeFavorite(favorite);
    }

    private Favorite getFavorite(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new FavoriteBadRequestException("존재하지 않는 즐겨찾기 id입니다", id));
    }

}
