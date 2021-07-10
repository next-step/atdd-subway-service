package nextstep.subway.favorite.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	@PostMapping
	public ResponseEntity createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favoriteResponse = FavoriteResponse.of(1L, new Station("강남역"), new Station("정자역"));
		return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
	}

	@GetMapping
	public ResponseEntity findFavorite() {
		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteFavorite(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
	}
}
