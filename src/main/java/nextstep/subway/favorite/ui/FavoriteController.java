package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
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

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.save(favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites() {
        return ResponseEntity.ok(favoriteService.findAllFavorites());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponse> findFavoriteById(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.findFavoriteResponseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavoriteById(id);
        return ResponseEntity.noContent().build();
    }
}
