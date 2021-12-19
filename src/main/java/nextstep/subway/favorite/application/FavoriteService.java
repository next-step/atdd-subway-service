package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    public static final String MESSAGE_NO_FAVORITE = "해당 ID의 즐겨찾기가 없습니다";
    private final MemberService memberService;

    private final StationService stationService;

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberService memberService, StationService stationService, FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.stationService = stationService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station sourceStation = stationService.findStationById(request.getSourceStationId());
        Station targetStation = stationService.findStationById(request.getTargetStationId());
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        Favorite persistFavorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(persistFavorite);
    }

    public List<FavoriteResponse> findFavorites(Long memberId) {
        Member member = memberService.findMemberById(memberId);
        List<Favorite> favorites = member.getFavorites();
        return FavoriteResponse.from(favorites);
    }

    public void deleteFavorite(Long loginMemberId, Long id) {
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MESSAGE_NO_FAVORITE));
        if (favorite.isLoginMemberFavorite(loginMemberId)) {
            favoriteRepository.deleteById(id);
        }
    }
}
