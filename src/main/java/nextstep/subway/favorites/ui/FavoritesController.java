package nextstep.subway.favorites.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    @PostMapping
    public ResponseEntity<Void> createFavorites(FavoritesRequest favoritesRequest) {
        // TODO 변경
        return ResponseEntity.created(URI.create("/favorites/" + 1)).build();
    }
}
