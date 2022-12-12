package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody FavoriteCreateRequest request) {
        Long favoriteId = favoriteService.createFavorite(loginMember, request);
        return ResponseEntity.created(URI.create("/favorite/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findAllFavoritesBy(loginMember));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                            @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(loginMember, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
