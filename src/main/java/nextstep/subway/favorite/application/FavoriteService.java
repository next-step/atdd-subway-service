package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
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

    private static final String NOT_FOUND_FAVORITE_BY_ID_AND_MEMBER_ID = "즐겨찾기 정보를 찾을 수 없습니다. (id=%s, memberId=%s)";

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           MemberService memberService,
                           StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse createFavorites(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findById(loginMember.getId());
        Station source = stationService.findStationById(favoriteRequest.getSource());
        Station target = stationService.findStationById(favoriteRequest.getTarget());
        Favorite savedFavorite = favoriteRepository.save(Favorite.of(source, target, member));
        return FavoriteResponse.from(savedFavorite);
    }

    public List<FavoriteResponse> showFavorites(LoginMember loginMember) {
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
        return favorites.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFavorite(LoginMember loginMember, Long favoriteId) {
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMember.getId())
                .orElseThrow(() -> new IllegalStateException(
                        String.format(NOT_FOUND_FAVORITE_BY_ID_AND_MEMBER_ID, favoriteId, loginMember.getId())
                ));

        favoriteRepository.delete(favorite);
    }
}
