package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.dto.StationInFavoriteResponse;
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
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity getFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        StationInFavoriteResponse mockSource = new StationInFavoriteResponse(1L, "강남역", LocalDateTime.now(), LocalDateTime.now());
        StationInFavoriteResponse mockTarget = new StationInFavoriteResponse(2L, "남부터미널역", LocalDateTime.now(), LocalDateTime.now());

        return ResponseEntity.ok().body(Arrays.asList(new FavoriteResponse(1L, mockSource, mockTarget)));
    }
}
