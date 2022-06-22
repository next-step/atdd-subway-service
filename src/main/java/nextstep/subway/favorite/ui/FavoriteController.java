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
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest request) {
        Long favoriteId = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> response = favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
