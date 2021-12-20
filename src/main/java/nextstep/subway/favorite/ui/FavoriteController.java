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
@RequestMapping("favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("")
    public ResponseEntity<FavoriteResponse> createLine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        final FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<FavoriteResponse> findLineById(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.findLineResponseById(loginMember, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteLineById(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
