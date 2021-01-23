package nextstep.subway.favorite.ui;

import static java.time.LocalDateTime.*;

import java.net.URI;
import java.util.Arrays;
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

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	public Long tempId = 1L;

	@PostMapping
	public ResponseEntity createFavorite(@RequestBody FavoriteRequest favoriteRequest) {
		return ResponseEntity.created(URI.create("/favorites/" + tempId++)).build();

	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FavoriteResponse>> showFavorites() {
		List<FavoriteResponse> mockResponse = Arrays.asList(
			new FavoriteResponse(
				1L,
				new StationResponse(1L, "강남역", now(), now()),
				new StationResponse(2L, "양재역", now(), now())),
			new FavoriteResponse(
				2L,
				new StationResponse(2L, "양재역", now(), now()),
				new StationResponse(3L, "정자역", now(), now()))
		);
		return ResponseEntity.ok().body(mockResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteFavorite(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
	}
}
