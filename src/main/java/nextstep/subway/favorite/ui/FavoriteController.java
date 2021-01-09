package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<FavoriteResponse> create(@AuthenticationPrincipal LoginMember loginMember,
		  @RequestBody FavoriteRequest request) {
		FavoriteResponse response = favoriteService.create(loginMember.getId(), request);
		return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findAllFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> response = favoriteService
			  .findAllFavoritesOfMine(loginMember.getId());
		return ResponseEntity.ok(response);
	}

	@DeleteMapping
	public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
		favoriteService.deleteFavorite(loginMember.getId(), request);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleIllegalArgsException(RuntimeException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}
}
