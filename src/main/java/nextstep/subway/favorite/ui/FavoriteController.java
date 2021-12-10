package nextstep.subway.favorite.ui;

import java.net.*;
import java.util.*;
import java.util.stream.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import nextstep.subway.auth.domain.*;
import nextstep.subway.favorite.application.*;
import nextstep.subway.favorite.dto.*;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final FavoriteReadService favoriteReadService;

    public FavoriteController(final FavoriteService favoriteService, final FavoriteReadService favoriteReadService) {
        this.favoriteService = favoriteService;
        this.favoriteReadService = favoriteReadService;
    }

    @PostMapping
    public ResponseEntity createFavorites(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest) {
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        List<FavoriteResponse> favoriteResponses = favoriteReadService.findFavorites(loginMember)
            .stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());

        return ResponseEntity.ok().body(favoriteResponses);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.deleteFavorite(id, loginMember.getId());
        return ResponseEntity.noContent().build();
    }
}
