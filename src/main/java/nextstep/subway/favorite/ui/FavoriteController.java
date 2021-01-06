package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite() {
        return ResponseEntity.created(URI.create("/favorites/" + 1L)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites() {
        FavoriteResponse favorite = new FavoriteResponse(1L, null, null);
        return ResponseEntity.ok(Arrays.asList(favorite));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}
