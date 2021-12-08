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
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(final FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal final LoginMember loginMember,
		@RequestBody final FavoriteRequest favoriteRequest) {
		Favorite favorite = favoriteService.createFavorite(loginMember, favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(FavoriteResponse.of(favorite));
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findAll(@AuthenticationPrincipal final LoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.findAll(loginMember));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@AuthenticationPrincipal final LoginMember loginMember,
		@PathVariable("id") final Long id) {
		favoriteService.delete(loginMember, id);
		return ResponseEntity.noContent().build();
	}
}
