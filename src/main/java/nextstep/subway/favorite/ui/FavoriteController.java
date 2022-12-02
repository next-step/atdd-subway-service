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
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findAllFavoriteByMember(loginMember.getId()));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                @PathVariable(name = "favoriteId") Long favoriteId) {
        favoriteService.deleteFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
