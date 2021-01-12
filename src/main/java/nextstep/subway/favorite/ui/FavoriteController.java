package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.FavoriteException;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteReuqest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody FavoriteReuqest param) {
        FavoriteResponse response = favoriteService.createFavorite(loginMember.getId(), param);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> listResponse = favoriteService.findFavoritesOfMember(loginMember.getId());
        return ResponseEntity.ok().body(listResponse);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                         @PathVariable Long id) {
        favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(FavoriteException.class)
    public ResponseEntity handleIllegalArgsException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


}
