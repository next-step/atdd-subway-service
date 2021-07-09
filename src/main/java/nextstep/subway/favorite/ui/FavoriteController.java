package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity createFavorite(
			@AuthenticationPrincipal LoginMember loginMember,
			@RequestBody FavoriteRequest favoriteRequest
	) {
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(favoriteRequest, loginMember.getId());
		return ResponseEntity.created(URI.create("/favorites" + favoriteResponse.getFavoriteId())).body(favoriteResponse);
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
