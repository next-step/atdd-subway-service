package nextstep.subway.favorite.ui;

import java.net.URI;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity createMember(@AuthenticationPrincipal LoginMember loginMember,
                                       @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.saveFavorite(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }
}
