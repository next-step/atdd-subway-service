package nextstep.subway.favorite.ui;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.favorite.application.FavoriteService;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoritesResponse> add(@AuthenticationPrincipal LoginMember loginMember,
                                                 @Valid @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.add(loginMember, favoriteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<FavoritesResponse> search(@AuthenticationPrincipal LoginMember loginMember) {
        FavoritesResponse search = favoriteService.search(loginMember);
        return ResponseEntity.ok(search);
    }

    @DeleteMapping
    public ResponseEntity<FavoritesResponse> remove(@AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.remove(loginMember);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<FavoritesResponse> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest()
                .build();
    }
}
