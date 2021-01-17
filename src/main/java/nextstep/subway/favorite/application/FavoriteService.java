package nextstep.subway.favorite.application;

import nextstep.subway.advice.exception.FavoriteBadRequestException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(final MemberService memberService, final FavoriteRepository favoriteRepository) {
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        List<Favorite> favorites = memberService.findFavorites(id);

        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
        return memberService.addFavorite(loginMember.getId(), request);
    }

    public void deleteFavorite(LoginMember loginMember, Long id) {
        memberService.deleteFavorite(loginMember.getId(), getFavorite(id));
    }

    private Favorite getFavorite(Long id) {
        return favoriteRepository.findById(id).orElseThrow(() -> new FavoriteBadRequestException("존재하지 않는 즐겨찾기 id입니다", id));
    }
}
