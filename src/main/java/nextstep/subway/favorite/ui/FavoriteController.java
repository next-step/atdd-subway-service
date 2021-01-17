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
    FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorites(@AuthenticationPrincipal LoginMember loginMember,
                                          @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.create(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> favoriteList(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.list(loginMember.getId());
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> favoriteList(@AuthenticationPrincipal LoginMember loginMember,
                                       @PathVariable Long id) {
        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
