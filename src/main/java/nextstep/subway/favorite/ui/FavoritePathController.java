package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoritePathService;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {
    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<FavoritePathResponse> createFavoritePath(@AuthenticationPrincipal LoginMember loginMember,
                                                   @RequestBody FavoritePathRequest request) {
        FavoritePathResponse favoritePath = favoritePathService.createFavoritePath(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + favoritePath.getId())).body(favoritePath);
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> findFavoritePaths(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoritePathService.findFavoritePaths(loginMember));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoritePathResponse> findFavoritePathById(@PathVariable Long id) {
        return ResponseEntity.ok(favoritePathService.findFavoritePathById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavoritePath(@PathVariable Long id) {
        favoritePathService.deleteFavoritePath(id);
        return ResponseEntity.noContent().build();
    }
}
