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
import nextstep.subway.member.dto.MemberResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private final FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity<Void> createMyFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		Favorite savedFavorite = favoriteService.saveFavorite(loginMember, favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + savedFavorite.getId())).build();
	}

	@GetMapping
	public ResponseEntity<List<FavoriteResponse>> findMyAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteResponses(loginMember);
		return ResponseEntity.ok().body(favoriteResponses);
	}

	@DeleteMapping("{favoriteId}")
	public ResponseEntity<Void> deleteMyFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@PathVariable Long favoriteId) {
		favoriteService.deleteFavorite(loginMember, favoriteId);
		return ResponseEntity.noContent().build();
	}
}
