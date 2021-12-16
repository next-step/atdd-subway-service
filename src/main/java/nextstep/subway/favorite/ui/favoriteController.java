package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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
import nextstep.subway.favorite.domain.Favorite;

@RestController
@RequestMapping("/favorites")
public class favoriteController {
	private final FavoriteService favoriteService;

	public favoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember
										,@RequestBody FavoriteRequest favoriteRequest) {
		Favorite saveFavorite = favoriteService.save(loginMember, favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + saveFavorite.getId())).build();
	}

	@GetMapping()
	public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteResponses(loginMember);
		return ResponseEntity.ok().body(favoriteResponses);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember
										,@PathVariable("id") Long favoriteId) {
		favoriteService.delete(loginMember, favoriteId);
		return ResponseEntity.noContent().build();
	}


}
