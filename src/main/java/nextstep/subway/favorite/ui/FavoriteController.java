package nextstep.subway.favorite.ui;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.OptionalLoginMember;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.application.FavoriteValidationException;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal OptionalLoginMember loginMember) {
		return ResponseEntity.ok(favoriteService.getFavorites(loginMember.orElseAuthenticationThrow()));
	}

	@PostMapping
	public ResponseEntity postFavorites(@AuthenticationPrincipal OptionalLoginMember loginMember,
	                                    @RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember.orElseAuthenticationThrow(), favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity deleteFavorite(@AuthenticationPrincipal OptionalLoginMember loginMember,
	                                     @PathVariable(value = "id") Long favoriteId) {
		favoriteService.deleteFavoriteById(loginMember.orElseAuthenticationThrow(), favoriteId);
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity handleRuntimeException(RuntimeException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@ExceptionHandler(FavoriteValidationException.class)
	public ResponseEntity handleFavoriteValidationException(FavoriteValidationException e) {
		return ResponseEntity.badRequest().build();
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity handleFavoriteAuthorizationException(AuthorizationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
