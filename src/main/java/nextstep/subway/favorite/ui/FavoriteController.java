package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	@PostMapping
	public ResponseEntity createFavorite(
			@AuthenticationPrincipal LoginMember loginMember,
			@RequestBody FavoriteRequest favoriteRequest
	) {
		return ResponseEntity.created(URI.create("/favorites" + "id")).body("");
	}

	@GetMapping()
	public ResponseEntity getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok("");
	}

	@DeleteMapping("{favoriteId}")
	public ResponseEntity deleteFavorite(
			@AuthenticationPrincipal LoginMember loginMember,
			@PathVariable Long favoriteId
	) {
		return ResponseEntity.noContent().build();
	}
}
