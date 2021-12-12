package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorites(@RequestBody FavoriteRequest favoriteRequest) {
        Long id = favoriteService.createFavorites(favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAll() {
        return ResponseEntity.ok().body(favoriteService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorites(@PathVariable Long id) {
        favoriteService.deleteFavorites(id);
        return ResponseEntity.noContent().build();
    }
}
