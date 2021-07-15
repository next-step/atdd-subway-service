package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> createStation(@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favorite = favoriteService.saveFavorite(favoriteRequest);
		return ResponseEntity.created(URI.create("/stations/" + favorite.getId())).body(favorite);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FavoriteResponse>> showStations() {
		return ResponseEntity.ok().body(favoriteService.findAllFavorites());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteStation(@PathVariable Long id) {
		favoriteService.deleteFavoriteById(id);
		return ResponseEntity.noContent().build();
	}
}
