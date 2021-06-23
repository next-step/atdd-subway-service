package nextstep.subway.favorite.ui;

import static java.time.LocalDateTime.*;
import static java.util.Arrays.*;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;

@RestController
public class FavoriteController {

	@PostMapping("/favorites")
	public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		return ResponseEntity.created(URI.create("/favorites/1")).build();
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		StationResponse 강남역 = new StationResponse(1L, "강남역", now(), now());
		StationResponse 광교역 = new StationResponse(2L, "광교역", now(), now());
		FavoriteResponse favoriteResponse = new FavoriteResponse(1L, 강남역, 광교역);
		return ResponseEntity.ok(asList(favoriteResponse));
	}

	@DeleteMapping("/favorites/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable Long favoriteId) {
		return ResponseEntity.noContent().build();
	}

}
