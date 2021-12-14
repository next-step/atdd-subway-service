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
	public ResponseEntity<FavoriteResponse> add(
		@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest request
	) {
		final FavoriteResponse response = favoriteService.add(loginMember.getId(), request);
		final URI uri = URI.create(String.format("/favorites/%d", response.getId()));
		return ResponseEntity.created(uri)
			.body(response);
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> getAll(@AuthenticationPrincipal LoginMember loginMember) {
		final List<FavoriteResponse> favorites = favoriteService.getAll(loginMember.getId());
		return ResponseEntity.ok().body(favorites);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
		favoriteService.delete(loginMember.getId(), id);
		return ResponseEntity.noContent().build();
	}
}
