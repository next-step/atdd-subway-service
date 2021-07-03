package nextstep.subway.favorite.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

import static nextstep.subway.PageController.URIMapping.FAVORITES;

@RequiredArgsConstructor
@RestController
@RequestMapping(FAVORITES)
public class FavoriteController {
    private final FavoriteService service;

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favoriteResponse = service.createFavorite(loginMember.getId(), request);

        return ResponseEntity.created(URI.create(String.format("%s/%s", FAVORITES, favoriteResponse.getId())))
                .body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(service.findFavorites(loginMember.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable(name = "id") Long favoriteId) {
        service.deleteFavorite(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}