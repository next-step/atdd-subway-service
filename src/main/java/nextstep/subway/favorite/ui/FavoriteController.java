package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RestController
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping("/favorites")
	public ResponseEntity<FavoriteResponse> createStation(@RequestBody FavoriteRequest favoriteRequest
		, @AuthenticationPrincipal LoginMember loginMember) {
		FavoriteResponse favorite = favoriteService.saveFavorite(favoriteRequest, loginMember.getId());
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
	}

	@GetMapping(value = "/favorites", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FavoriteResponse>> showStations(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favorites = favoriteService.findAllFavoritesByMember(loginMember.getId());
		return ResponseEntity.ok().body(favorites);
	}

	@DeleteMapping("/favorites/{id}")
	public ResponseEntity deleteStation(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
		return ResponseEntity.badRequest().build();
	}
}
