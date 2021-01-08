package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(Collections.emptyList());
	}

	@PostMapping
	public ResponseEntity postFavorites(@AuthenticationPrincipal LoginMember loginMember,
	                                    @RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favoriteResponse = new FavoriteResponse();
		return ResponseEntity.created(URI.create("/favorites/" + 1)).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
	                                     @PathVariable(value = "id") Long favoriteId) {
		return ResponseEntity.noContent().build();
	}
}
