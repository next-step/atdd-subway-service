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

import static java.lang.String.format;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember, request);

        return ResponseEntity.created(URI.create(format("/favorites/%d", favorite.getId())))
                .body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> lists(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(
                favoriteService.findAllByMember(loginMember)
        );
    }
}
