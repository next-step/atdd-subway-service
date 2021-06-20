package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.application.FavoriteService;
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
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember.getId(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginMember.getId());

        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorites(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);

        return ResponseEntity.noContent().build();
    }
}
