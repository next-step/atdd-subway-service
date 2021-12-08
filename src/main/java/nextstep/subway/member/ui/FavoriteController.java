package nextstep.subway.member.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.FavoriteService;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * packageName : nextstep.subway.favorites.dto.ui
 * fileName : FavoriteController
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
@RequestMapping("favorites")
@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteResponse> saveFavorite(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        final FavoriteResponse favorite = favoriteService.addFavorite(member.getId(), request);
        return ResponseEntity.created(URI.create("/lines/" + favorite.getId())).body(favorite);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> responses = favoriteService.findFavorites(member.getId());
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember member, @PathVariable Long favoriteId) {
        favoriteService.deleteFavorite(member.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
