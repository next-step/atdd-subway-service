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

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping(FavoriteController.FAVORITES_PATH)
public class FavoriteController {
	public static final String FAVORITES_PATH = "/favorites";
	private final FavoriteService favoriteService;

	@PostMapping
	public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest request) {
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember.getId(), request);
		return ResponseEntity.created(URI.create(String.format("%s/%d", FAVORITES_PATH, favoriteResponse.getId())))
			.build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favorites = favoriteService.getFavoritesByMemberId(loginMember.getId());
		return ResponseEntity.ok().body(favorites);
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<Void> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable long favoriteId) {
		favoriteService.deleteFavorite(favoriteId);
		return ResponseEntity.noContent().build();
	}
}
