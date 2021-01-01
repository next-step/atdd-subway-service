package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import nextstep.subway.favorite.ui.dto.FavoriteResponse;
import nextstep.subway.favorite.ui.dto.StationInFavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    @PostMapping
    public ResponseEntity createFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest
    ) {
        // TODO: Impl
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        // TODO: Impl
        StationInFavoriteResponse mockSource = new StationInFavoriteResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
        StationInFavoriteResponse mockTarget = new StationInFavoriteResponse(2L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now());

        return ResponseEntity.ok().body(Arrays.asList(new FavoriteResponse(1L, mockSource, mockTarget)));
    }

    @DeleteMapping("/{favoriteId}")
    public ResponseEntity deleteFavorite(
            @AuthenticationPrincipal LoginMember loginMember, @PathVariable Long favoriteId
    ) {
        return ResponseEntity.noContent().build();
    }
}
