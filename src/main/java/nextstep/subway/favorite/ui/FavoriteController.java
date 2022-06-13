package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.findFavoriteById(id));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> response = favoriteService.findAllFavoritesByMemberId(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavoriteById(id);
        return ResponseEntity.noContent().build();
    }
}
