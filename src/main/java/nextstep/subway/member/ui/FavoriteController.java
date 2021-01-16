package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.FavoriteService;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity addFavorite(@AuthenticationPrincipal LoginMember loginMember
            , @RequestBody FavoriteRequest favoriteRequest) {
        Long id = this.favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse favoriteResponse = this.favoriteService.findFavorites(loginMember.getId());
        System.out.println("즐겨찾기!!!!!!" + favoriteResponse);
        return ResponseEntity.ok(favoriteResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        this.favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
