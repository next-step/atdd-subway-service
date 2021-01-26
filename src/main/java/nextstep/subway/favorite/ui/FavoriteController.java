package nextstep.subway.favorite.ui;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	@PostMapping
	public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		return ResponseEntity.created(URI.create("/favorites/" + loginMember.getId())).build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		StationResponse source = new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
		StationResponse target = new StationResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
		FavoriteResponse favoriteResponse = new FavoriteResponse(1L, source, target);
		return ResponseEntity.ok().body(Collections.singletonList(favoriteResponse));
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.noContent().build();
	}
}
