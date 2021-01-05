package nextstep.subway.favorites.ui;

import nextstep.subway.favorites.domain.Favorite;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.favorites.service.FavoritesService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    private final FavoritesService favoritesService;

    public FavoriteController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createFavorite(@RequestBody FavoritesRequest favoritesRequest) {
        Favorite favorite = favoritesService.createFavorite(favoritesRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<FavoritesResponse> findFavorites() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoritesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
