package nextstep.subway.domain.favorite.ui;

import nextstep.subway.domain.auth.domain.AuthenticationPrincipal;
import nextstep.subway.domain.auth.domain.User;
import nextstep.subway.domain.favorite.application.FavoriteService;
import nextstep.subway.domain.favorite.dto.FavoriteRequest;
import nextstep.subway.domain.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal User user, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.createFavorite(user, request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal User user) {
        List<FavoriteResponse> favorite = favoriteService.findFavorite(user);
        return ResponseEntity.ok().body(favorite);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal User user, @PathVariable("favoriteId") Long favoriteId) {
        favoriteService.deleteFavorite(user, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
