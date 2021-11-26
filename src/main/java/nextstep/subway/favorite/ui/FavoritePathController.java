package nextstep.subway.favorite.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoritePathService;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {
    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavoritePath(@AuthenticationPrincipal LoginMember loginMember,
                                                   @RequestBody FavoritePathRequest request) {
        FavoritePathResponse favoritePath = favoritePathService.createFavoritePath(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + favoritePath.getId())).build();
    }
}
