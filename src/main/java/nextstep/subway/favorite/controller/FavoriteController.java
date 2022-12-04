package nextstep.subway.favorite.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.service.FavoriteService;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<?> createFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteService.createFavorite(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping("/favorites/{id}")
    public ResponseEntity<FavoriteResponse> findFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id) {
        FavoriteResponse favorite = favoriteService.findFavoriteById(loginMember.getId(), id);
        return ResponseEntity.ok().body(favorite);
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favorite = favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok().body(favorite);
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<?> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id) {
        favoriteService.deleteFavoriteById(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
