package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoriteService favoriteService;

    public FavoritesController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorites(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest favoriteRequest
    ) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorites(loginMember.getId(), favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public void findFavorites() {
        // TODO 찾기 기능 추가
    }

    @DeleteMapping
    public void removeFavorites() {
        // TODO 삭제 기능 추가
    }
}
