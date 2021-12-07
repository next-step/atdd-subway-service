package nextstep.subway.favorites.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.dto.FavoriteRequest;
import nextstep.subway.favorites.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> saveFavorite(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        memberService.addFavorite(member.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> responses = memberService.findFavorites(member.getId());
        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping("{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember member, @PathVariable Long favoriteId) {
        memberService.deleteFavorite(member.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
