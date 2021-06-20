package nextstep.subway.favorite.ui;

import java.net.URI;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.NotFoundStationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(value = "/favorites")
    public ResponseEntity<URI> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                              @RequestBody FavoriteRequest favoriteRequest) {
        return ResponseEntity.created(URI.create(
            "/favorites" + favoriteService.save(loginMember.getId(),
                                                favoriteRequest.getSource(),
                                                favoriteRequest.getTarget()))).build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                                         FavoriteRequest favoriteRequest) {
        return null;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
                                               FavoriteRequest favoriteRequest) {
        return null;
    }

    @ExceptionHandler(NotFoundStationException.class)
    public ResponseEntity<String> handleNotFoundStationException(NotFoundStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
