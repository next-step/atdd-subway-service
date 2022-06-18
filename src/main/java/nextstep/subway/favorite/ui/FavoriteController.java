package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse favorites = favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @PostMapping
    public ResponseEntity updateFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                                @RequestBody FavoriteRequest param) {
        favoriteService.updateFavorites(loginMember.getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
