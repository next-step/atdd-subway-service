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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationService stationService;
    private final MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        validateIsNull(loginMember, favoriteRequest);
        validateIsSameSourceAndTarget(favoriteRequest);
        Favorite favorite = saveFavorite(loginMember, favoriteRequest);
        return FavoriteResponse.of(favorite);
    }

    private Favorite saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getSource());
        return favoriteRepository.save(new Favorite(source, target, member));
    }

    private void validateIsNull(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        if (loginMember.getId() == null || favoriteRequest.getSource() == null || favoriteRequest.getTarget() == null) {
            throw new IllegalArgumentException("즐겨찾기 생성 파라미터 유효성 검증 실패하였습니다.");
        }
    }

    private void validateIsSameSourceAndTarget(FavoriteRequest favoriteRequest) {
        if (favoriteRequest.getSource() == favoriteRequest.getTarget()) {
            throw new IllegalArgumentException("출발역과 도착역이 같을 경우, 즐겨찾기 생성 불가합니다.");
        }
    }


    public List<FavoriteResponse> findAllFavorites(LoginMember loginMember) {
        Member member = memberService.findMemberById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
