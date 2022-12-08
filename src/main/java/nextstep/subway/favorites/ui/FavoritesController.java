package nextstep.subway.favorites.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.application.FavoritesService;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;

    public FavoritesController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @PostMapping("")
    public ResponseEntity<FavoritesResponse> createFavorites(@AuthenticationPrincipal LoginMember loginMember,
                                                             @RequestBody FavoritesRequest param) {
        FavoritesResponse favoritesResponse = favoritesService.createFavorites(loginMember.getId(), param);
        return ResponseEntity.created(URI.create("/favorites/" + favoritesResponse.getId())).build();
    }

    @GetMapping("")
    public ResponseEntity<List<FavoritesResponse>> retrieveFavoritesList(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoritesResponse> favoritesList = favoritesService.retrieveFavoritesList(loginMember.getId());
        return ResponseEntity.ok().body(favoritesList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal LoginMember loginMember,
                                                             @PathVariable("id") Long favoritesId) {
        favoritesService.deleteMember(loginMember.getId(), favoritesId);
        return ResponseEntity.noContent().build();
    }
}
