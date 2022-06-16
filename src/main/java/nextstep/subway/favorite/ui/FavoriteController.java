package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.service.FavoriteServiceFacade;
import nextstep.subway.line.application.PathServiceFacade;
import nextstep.subway.line.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteServiceFacade favoriteServiceFacade;
    private final PathServiceFacade pathServiceFacade;

    public FavoriteController(FavoriteServiceFacade favoriteServiceFacade,
        PathServiceFacade pathServiceFacade) {
        this.favoriteServiceFacade = favoriteServiceFacade;
        this.pathServiceFacade = pathServiceFacade;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavoriteList(
        @AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteList = favoriteServiceFacade.getFavoriteList(
            loginMember.getId());
        return ResponseEntity.ok().body(favoriteList);
    }

    @PostMapping
    public ResponseEntity saveFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest) {

        PathResponse path = pathServiceFacade.findPath(favoriteRequest.getSource(),
            favoriteRequest.getTarget());

        Favorite favorite = favoriteServiceFacade.saveFavorite(loginMember.getId(),
            favoriteRequest.getSource(), favoriteRequest.getTarget());
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id) {
        favoriteServiceFacade.deleteFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
