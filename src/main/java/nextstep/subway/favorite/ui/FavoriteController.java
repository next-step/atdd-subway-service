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

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> response = favoriteService.findFavorites(member.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> saveFavorite(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.saveFavorite(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal LoginMember member, @PathVariable Long favoriteId) {
        favoriteService.removeFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }
}
