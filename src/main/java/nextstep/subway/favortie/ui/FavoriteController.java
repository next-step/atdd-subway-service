package nextstep.subway.favortie.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favortie.application.FavoriteService;
import nextstep.subway.favortie.dto.FavoriteRequest;
import nextstep.subway.favortie.dto.FavoriteResponse;

@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@GetMapping("/favorites")
	public ResponseEntity<List<FavoriteResponse>> getAll(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favorites = favoriteService.getAll(loginMember);
		return ResponseEntity.ok().body(favorites);
	}

	@PostMapping("/favorites")
	public ResponseEntity create(@RequestBody FavoriteRequest request,
		@AuthenticationPrincipal LoginMember loginMember) {
		FavoriteResponse response = favoriteService.create(loginMember, request);
		return ResponseEntity
			.created(URI.create("/favorites/" + response.getId()))
			.build();
	}

	@DeleteMapping("/favorites/{id}")
	public ResponseEntity delete(@PathVariable Long id, @AuthenticationPrincipal LoginMember loginMember) {
		favoriteService.delete(loginMember, id);
		return ResponseEntity.noContent().build();
	}
}
