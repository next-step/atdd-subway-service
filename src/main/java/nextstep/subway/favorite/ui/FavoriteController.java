package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.favorite.dto.FavoriteCreateResponse;
import nextstep.subway.favorite.dto.FavoriteReadResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	@PostMapping("")
	public ResponseEntity<FavoriteCreateResponse> createFavorites() {
		return ResponseEntity.created(URI.create("")).build();
	}

	@GetMapping("")
	public ResponseEntity<FavoriteReadResponse> readFavorites() {
		return ResponseEntity.ok(new FavoriteReadResponse(Collections.emptyList()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite() {
		return ResponseEntity.noContent().build();
	}
}
