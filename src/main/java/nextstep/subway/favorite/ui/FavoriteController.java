package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {

    @PostMapping("/favorites")
    public ResponseEntity addFavorite(@RequestBody FavoriteRequest request) {

        return ResponseEntity.created(URI.create("/favorites/1")).build();
    }
}
