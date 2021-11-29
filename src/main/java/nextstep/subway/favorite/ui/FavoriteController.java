package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity addFavorite(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        Favorite favorite = favoriteService.add(member, request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorite(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> responseList = favoriteService.getFavorites(member).stream()
                                                            .map(favorite -> new FavoriteResponse(favorite.getId(), favorite.getSource(), favorite.getTarget()))
                                                            .collect(Collectors.toList());

        return ResponseEntity.ok().body(responseList);
    }

    @DeleteMapping(value = "/{favoriteId}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember member, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(favoriteId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity IllegalArgumentExceptionHandler() {
        return ResponseEntity.badRequest().build();
    }
}
