package nextstep.subway.favorite.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	private FavoriteService favoriteService;

	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@PostMapping
	public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember,@RequestBody FavoriteRequest request) {
		FavoriteResponse favoriteResponse = favoriteService.save(loginMember, request);
		return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
	}

	@GetMapping
	public ResponseEntity findFavoritesOfMine(@AuthenticationPrincipal LoginMember loginMember) {
		return ResponseEntity.ok().body(favoriteService.findFavoritesByMemberId(loginMember.getId()));
	}
}
