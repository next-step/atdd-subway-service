package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(final FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@AuthenticationPrincipal final LoginMember loginMember,
        @RequestBody final FavoriteRequest favoriteRequest) {

        final FavoriteResponse favoriteResponse = favoriteService.createFavoriteSection(loginMember, favoriteRequest);

        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> showFavoriteSections(
        @AuthenticationPrincipal final LoginMember loginMember) {

        return ResponseEntity.ok(favoriteService.findFavoriteSectionsByMember(loginMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFavoriteSection(@AuthenticationPrincipal final LoginMember loginMember,
        @PathVariable("id") final Long id) {

        favoriteService.deleteFavoriteSection(loginMember, id);

        return ResponseEntity.noContent().build();
    }
}
