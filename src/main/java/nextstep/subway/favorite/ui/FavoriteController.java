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

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(final FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findAll(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.findAll(loginMember.getId()));
	}

	@PostMapping
	public ResponseEntity<Void> saveFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favorite = favoriteService.save(loginMember.getId(), favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable Long favoriteId) {
		favoriteService.delete(loginMember.getId(), favoriteId);
		return ResponseEntity.noContent().build();
	}

}
