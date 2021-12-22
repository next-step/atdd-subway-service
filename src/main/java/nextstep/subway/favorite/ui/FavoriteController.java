package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.Member;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@AuthenticationPrincipal Member loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        Favorite favorite = favoriteService.createFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity findAllFavorites(@AuthenticationPrincipal Member loginMember) {
        if (!loginMember.isLoginMember()) {
            return ResponseEntity.ok().body(MemberResponse.nonLoginMemberResponse());
        }
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavorites(loginMember.getId());
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal Member loginMember, @PathVariable Long favoriteId) {
        if (!loginMember.isLoginMember()) {
            return ResponseEntity.ok().body(MemberResponse.nonLoginMemberResponse());
        }
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }
}
