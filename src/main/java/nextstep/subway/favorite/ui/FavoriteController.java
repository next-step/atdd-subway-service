package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("favorites")
    public ResponseEntity<FavoriteResponse> addFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favorite = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites" + favorite.getId())).build();
    }

}
