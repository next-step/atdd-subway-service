package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/favorites")
@RestController
public class FavoriteController {

    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity create(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        FavoriteResponse response = favoriteService.create(member.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getList(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> response = favoriteService.getList(member.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal LoginMember member, @PathVariable Long id) {
        favoriteService.delete(member.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
