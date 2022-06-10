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
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<?> create(@AuthenticationPrincipal LoginMember loginMember,
                                    @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = favoriteService.create(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findAll(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findAll(loginMember));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal LoginMember loginMember,
                                    @PathVariable Long id) {
        favoriteService.deleteById(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
