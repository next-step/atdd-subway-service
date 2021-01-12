package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteDeleteRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@RequestBody FavoriteRequest request,
                                         @AuthenticationPrincipal LoginMember loginMember) {
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(loginMember.getId(), request.getSource(), request.getTarget());
        FavoriteResponse favorite = favoriteService.createFavorite(favoriteCreateRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorites = favoriteService.findFavorite(loginMember.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        FavoriteDeleteRequest favoriteDeleteRequest = new FavoriteDeleteRequest(loginMember.getId(), id);
        favoriteService.deleteFavorite(favoriteDeleteRequest);
        return ResponseEntity.noContent().build();
    }

}
