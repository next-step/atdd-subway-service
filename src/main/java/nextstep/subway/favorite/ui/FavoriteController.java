package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity saveFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember, request);
        return ResponseEntity.created(URI.create("/favorite/" + favorite.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findFavorites(loginMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavoriteById(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
