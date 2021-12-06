package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> addFavorite(
		@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest
	) {
		FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> showFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok().body(favoriteService.findAllFavorites(loginMember.getId()));
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(
		@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable String favoriteId
	) {
		favoriteService.deleteFavorite(loginMember.getId(), favoriteId);
		return ResponseEntity.noContent().build();
	}
}
