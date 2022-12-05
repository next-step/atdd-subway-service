package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NotValidMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorites(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService
            .saveFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(
        @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.getFavorites(loginMember.getId()));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable("id") Long id,
        @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.removeFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotValidMemberException.class)
    public ResponseEntity<Void> forbidden(NotValidMemberException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
