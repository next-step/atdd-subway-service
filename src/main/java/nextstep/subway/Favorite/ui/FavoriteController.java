package nextstep.subway.Favorite.ui;

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

import nextstep.subway.Favorite.application.FavoriteService;
import nextstep.subway.Favorite.dto.FavoriteRequest;
import nextstep.subway.Favorite.dto.FavoritesResponse;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoritesResponse> createFavorite(
		@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
		FavoritesResponse response = favoriteService.saveFavorite(loginMember, request);
		return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
	}

	@GetMapping
	public ResponseEntity<List<FavoritesResponse>> findAllFavorites(
		@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok().body(favoriteService.findAllFavorites(loginMember));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite(
		@AuthenticationPrincipal LoginMember loginMember, @PathVariable long id) {
		favoriteService.deleteFavorite(loginMember, id);
		return ResponseEntity.noContent().build();
	}
}
