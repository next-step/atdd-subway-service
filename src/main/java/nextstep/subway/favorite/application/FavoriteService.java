package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.EntityNotFoundException;
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
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberService.memberById(loginMember.getId());
        Station source = stationService.stationById(favoriteRequest.getSource());
        Station target = stationService.stationById(favoriteRequest.getTarget());
        Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));

        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.memberById(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
        Member member = memberService.memberById(loginMember.getId());
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(
                () -> new EntityNotFoundException("즐겨찾기가 존재하지 않습니다.", favoriteId)
        );
        if (favorite.isNotAuthor(member)) {
            throw new AuthorizationException("자신의 즐겨찾기만 삭제할 수 있습니다.");
        }
        favoriteRepository.delete(favorite);
    }
}
