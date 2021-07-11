package nextstep.subway.favorite.controller;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        Favorite savedFavorite = favoriteService.save(loginMember, request);
        return ResponseEntity.created(URI.create("/favorites/" + savedFavorite.getId())).body(savedFavorite);
    }

    @GetMapping
    public ResponseEntity<FavoriteResponses> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponses favorites = favoriteService.findAllByMember(loginMember);
        return ResponseEntity.ok(favorites);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.delete(loginMember, id);
        return ResponseEntity.noContent().build();
    }
}
