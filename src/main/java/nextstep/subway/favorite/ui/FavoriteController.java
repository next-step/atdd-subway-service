package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(
                favoriteService.getAllFavoriteByOwner(loginMember.getId())
                        .toResponse()
        );
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest
    ) {
        FavoriteResponse response = FavoriteResponse
                .of(favoriteService.saveFavorite(favoriteRequest, loginMember.getId()));
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }
}
