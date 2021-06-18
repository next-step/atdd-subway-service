package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteCommandService;
import nextstep.subway.favorite.application.FavoriteQueryService;
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
    private final FavoriteQueryService favoriteQueryService;
    private final FavoriteCommandService favoriteCommandService;

    public FavoriteController(FavoriteQueryService favoriteQueryService, FavoriteCommandService favoriteCommandService) {
        this.favoriteQueryService = favoriteQueryService;
        this.favoriteCommandService = favoriteCommandService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteCommandService.createFavorite(loginMember, request);

        return ResponseEntity.created(URI.create(format("/favorites/%d", favorite.getId())))
                .body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> lists(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> results = favoriteQueryService.findAllByMember(loginMember);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteCommandService.deleteById(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
