package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(member, request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember member) {
        return ResponseEntity.ok().body(favoriteService.findFavorites(member.getId()));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember member, @PathVariable Long id) {
        favoriteService.deleteFavorite(member, id);
        return ResponseEntity.noContent().build();
    }

}
