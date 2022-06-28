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

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> addFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> fetchFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        final List<FavoriteResponse> favoriteResponses = favoriteService.fetchFavorites(loginMember.getId());
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorites(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

}
