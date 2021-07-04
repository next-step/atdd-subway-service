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
public class FavoriteController {

    private FavoriteService favoritesService;

    public FavoriteController(FavoriteService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorites(@AuthenticationPrincipal LoginMember loginMember,
                                          @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoritesService.saveFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoritesService.findFavorites(loginMember));
    }


    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @PathVariable Long id) {
        favoritesService.deleteFavorite(loginMember, id);
        return ResponseEntity.noContent().build();
    }

}
