package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest favoriteRequest, @AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest, loginMember);
        return ResponseEntity.created(URI.create("favorites/" + favoriteResponse.getId()))
                .body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
