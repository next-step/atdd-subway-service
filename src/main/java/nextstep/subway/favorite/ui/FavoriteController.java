package nextstep.subway.favorite.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
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

	@PostMapping
	public ResponseEntity<?> saveFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody FavoriteRequest favoriteRequest) {
		FavoriteResponse favorite = favoriteService.save(loginMember.getId(), favoriteRequest);
		return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
	}

}
