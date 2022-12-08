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

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest request
    ) {
        FavoriteResponse result = favoriteService.saveFavorite(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + result.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorite(
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        List<FavoriteResponse> result = favoriteService.getFavorite(loginMember);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable Long favoriteId
    ) {
        favoriteService.deleteFavorite(loginMember, favoriteId);
        return ResponseEntity.noContent().build();
    }
}
