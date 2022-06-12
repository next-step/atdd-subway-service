package nextstep.subway.favorite.ui;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember){
        List<FavoriteResponse> favorites =  favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember
            , @RequestBody FavoriteCreateRequest favoriteCreateRequest
    ) throws URISyntaxException {
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember.getId(), favoriteCreateRequest);
        return ResponseEntity.created(new URI("/favorites")).body(favoriteResponse);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @PathVariable(name = "favoriteId") String favoriteId){
        favoriteService.deleteFavorite(Long.parseLong(favoriteId));
        return ResponseEntity.noContent().build();
    }
}
