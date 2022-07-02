package nextstep.subway.favorites.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.application.FavoritesService;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public ResponseEntity<FavoritesResponse> saveFavorites(
        @AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoritesRequest favoritesRequest
    ) {
        FavoritesResponse favoritesResponse = favoritesService.saveFavorites(loginMember, favoritesRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoritesResponse.getId())).body(favoritesResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoritesResponse>> getAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoritesService.findAllFavorites(loginMember));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteFavoritesById(
        @AuthenticationPrincipal LoginMember loginMember,
        @PathVariable("id") Long id
    ) {
        favoritesService.deleteFavoritesById(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
