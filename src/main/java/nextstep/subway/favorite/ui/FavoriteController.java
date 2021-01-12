package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> create(@AuthenticationPrincipal LoginMember member,
                                                   @RequestParam Long source, @RequestParam Long target) {
        FavoriteResponse response = favoriteService.create(member.getId(), source, target);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAll(@AuthenticationPrincipal LoginMember member) {
        List<FavoriteResponse> responses = favoriteService.findByMember(member.getId());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        favoriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
