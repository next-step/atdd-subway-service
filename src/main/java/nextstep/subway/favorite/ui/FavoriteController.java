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
@RequestMapping("/favorite")
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favorite = favoriteService.saveFavorite(loginMember.getId(), favoriteRequest);
		return ResponseEntity.created(URI.create("/favorite/" + favorite.getId())).body(favorite);
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.findFavorites(loginMember.getId()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
		favoriteService.deleteFavoriteById(loginMember.getId(), id);
		return ResponseEntity.noContent().build();
	}
}
