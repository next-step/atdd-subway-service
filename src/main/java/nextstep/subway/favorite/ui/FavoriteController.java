package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest favoriteRequest) {

        FavoriteResponse response = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        final List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginMember.getId());

        return ResponseEntity.ok(favoriteResponses);
    }

    @GetMapping("/favorites/{favoriteId}")
    public ResponseEntity<FavoriteResponse> findFavorite(@PathVariable long favoriteId) {
        final FavoriteResponse favoriteResponses = favoriteService.findFavorite(favoriteId);
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> findFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable long favoriteId) {
        favoriteService.deleteFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException2(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}