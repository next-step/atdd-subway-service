package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
  @PostMapping(value = "/favorites", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity createMember(@RequestBody FavoriteRequest request) {
    return ResponseEntity.created(URI.create("/favorites/" + 1)).build();
  }
}
