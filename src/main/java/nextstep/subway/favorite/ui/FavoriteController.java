package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.MediaType;
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
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<FavoriteResponse> getFavoriteById(@PathVariable Long id) {
        return ResponseEntity.ok().body(favoriteService.findFavoriteById(id));
    }

    @GetMapping("/favorites/me")
    public ResponseEntity<List<FavoriteResponse>> getFavoriteMe(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoriteService.findAllFavoritesByMemberId(loginMember.getId()));
    }

    @PostMapping(path = "/favorites/me", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FavoriteResponse> addFavoriteMe(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorite/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @DeleteMapping("/favorites/me")
    public ResponseEntity<FavoriteResponse> deleteFavoriteMe(@AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.deleteFavorite(loginMember.getId());
        return ResponseEntity.noContent().build();
    }

}
