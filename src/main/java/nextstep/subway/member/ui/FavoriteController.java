package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.application.FavoriteService;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
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

    /**
     * 해당 사용자에게 주어진 즐겨찾기를 추가합니다.
     * @param loginMember
     * @param favoriteRequest
     * @return
     */
    @PostMapping
    public ResponseEntity addFavorite(@AuthenticationPrincipal LoginMember loginMember
            , @RequestBody FavoriteRequest favoriteRequest) {
        Long id = this.favoriteService.addFavorite(loginMember.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + id)).build();
    }

    /**
     * 해당 사용자의 모든 즐겨찾기를 찾습니다.
     * @param loginMember
     * @return
     */
    @GetMapping
    public ResponseEntity<FavoriteResponse> findFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        FavoriteResponse favoriteResponse = this.favoriteService.findFavorites(loginMember.getId());
        return ResponseEntity.ok(favoriteResponse);
    }

    /**
     * 해당 사용자의 주어진 즐겨찾기를 삭제합니다.
     * @param loginMember
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        this.favoriteService.deleteFavorite(loginMember.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
