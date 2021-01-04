package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import nextstep.subway.favorite.ui.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest
    ) {
        Long favoriteId = favoriteService.saveFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteId)).build();
    }

    @GetMapping
    public ResponseEntity getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.getFavorites(loginMember);

        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long favoriteId
    ) {
        favoriteService.deleteFavorite(loginMember, favoriteId);

        return ResponseEntity.noContent().build();
    }
}
