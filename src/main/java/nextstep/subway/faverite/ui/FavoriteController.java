package nextstep.subway.faverite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

	@PostMapping
	public ResponseEntity<?> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
		@RequestBody PathRequest pathRequest) {
		return ResponseEntity.created(URI.create("/favorites/" + loginMember.getId())).build();
	}

	@GetMapping
	public ResponseEntity<List<PathResponse>> showFavorite(@AuthenticationPrincipal LoginMember loginMember) {

		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping
	public void deleteFavorite() {

	}
}
