package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping(value = "/favorites")
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.create(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).build();
    }

    @GetMapping(value = "/favorites")
    public ResponseEntity<List<FavoriteResponse>> findAll(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> result = favoriteService.findAll(loginMember);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/favorites/{id}")
    public ResponseEntity deleteFavorites(@PathVariable Long id){
        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
