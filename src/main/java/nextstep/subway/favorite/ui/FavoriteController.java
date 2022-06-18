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
    public ResponseEntity createFavoriteOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavoritesByMemberId(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavoriteOfMine(@AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
