package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoritePathService;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoritePathController {

    private final FavoritePathService favoritePathService;

    public FavoritePathController(FavoritePathService favoritePathService) {
        this.favoritePathService = favoritePathService;
    }

    @PostMapping
    public ResponseEntity<FavoritePathResponse> create(@AuthenticationPrincipal LoginMember loginMember,
                                                       @RequestBody FavoritePathRequest request) {
        FavoritePathResponse favoritePath = favoritePathService.save(loginMember.getId(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favoritePath.getId())).body(favoritePath);
    }

    @GetMapping
    public ResponseEntity<List<FavoritePathResponse>> findAll(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok().body(favoritePathService.findAll(loginMember.getId()));
    }

    @DeleteMapping(value = "/{favoriteId}")
    public ResponseEntity<FavoritePathResponse> delete(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long favoriteId) {
        favoritePathService.delete(favoriteId);
        return ResponseEntity.noContent().build();
    }
}
