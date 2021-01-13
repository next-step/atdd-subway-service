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

	public FavoriteController(final FavoriteService service) {
		this.favoriteService = service;
	}

	@PostMapping
	public ResponseEntity<?> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest request) {
		FavoriteResponse response = favoriteService.createFavorite(loginMember.getId(), request);
		return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> showFavorite(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.findFavorites(loginMember.getId()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable long id) {
		favoriteService.deleteFavoriteById(id);
		return ResponseEntity.noContent().build();
	}
}
