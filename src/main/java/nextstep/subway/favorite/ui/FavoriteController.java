package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(
            @RequestBody FavoriteRequest favoriteRequest,
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        FavoriteResponse favorite = favoriteService.saveFavorite(favoriteRequest, loginMember.getId());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorite(
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        List<FavoriteResponse> favorite = favoriteService.findAllFavorite(loginMember.getId());
        return ResponseEntity.ok(favorite);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> deleteFavorite(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
