package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody FavoriteRequest request,
            @AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.created(URI.create("/favorites/" + favoriteService.create(request, loginMember))).build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> get(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse response = favoriteService.get(loginMember);
        if (response.empty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long id,
            @AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.delete(id, loginMember);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity illegalArgunemtExceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
