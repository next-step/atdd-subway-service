package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class FavoriteController {
    private FavoriteService favoriteService;

    private FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> addFavoriteSection(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        favoriteService.addFavoriteSection(loginMember.getId(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findFavoriteSection(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findFavoriteSection(loginMember.getId()));
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<FavoriteResponse> deleteFavoriteSection(@AuthenticationPrincipal LoginMember loginMember, @RequestBody Map<String, Long> request) {
        favoriteService.deleteFavoriteSection(loginMember.getId(), request.get("deleteFavoriteId"));
        return ResponseEntity.noContent().build();
    }

}
