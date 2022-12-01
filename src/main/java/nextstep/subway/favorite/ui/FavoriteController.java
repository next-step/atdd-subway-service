package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember member,
                                               @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(member, request);
        return ResponseEntity.created(getLocation(favoriteResponse)).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorite(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorite(member);
        return ResponseEntity.ok(favoriteResponses);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal LoginMember member,
                                               @PathVariable long favoriteId) {
        favoriteService.removeFavorite(member, favoriteId);
        return ResponseEntity.noContent().build();
    }

    private static URI getLocation(FavoriteResponse favoriteResponse) {
        return URI.create("/favorites/" + favoriteResponse.getId());
    }
}
