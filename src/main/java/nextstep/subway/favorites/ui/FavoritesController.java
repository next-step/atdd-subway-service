package nextstep.subway.favorites.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.application.FavoritesService;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorites(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoritesRequest favoritesRequest) {
        FavoritesResponse response = favoritesService.createFavorites(loginMember, favoritesRequest);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }
}
