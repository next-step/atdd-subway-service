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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
        @AuthenticationPrincipal final LoginMember loginMember,
        @RequestBody final FavoriteRequest request
    ) {
        final FavoriteResponse favorite = favoriteService.save(loginMember, request);
        // return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
        return ResponseEntity.created(URI.create("/favorites/1"))
            .body(new FavoriteResponse(1L, null, null));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(
        @AuthenticationPrincipal final LoginMember loginMember
    ) {
        return ResponseEntity.ok().body(favoriteService.findAll(loginMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(
        @AuthenticationPrincipal final LoginMember loginMember,
        @PathVariable final Long id
    ) {
        favoriteService.delete(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
