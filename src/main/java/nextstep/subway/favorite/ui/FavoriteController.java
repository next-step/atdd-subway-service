package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService service;

    public FavoriteController(FavoriteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<List<FavoriteResponse>> createFavorite(
        @AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = service.saveFavorite(member, request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites(
        @AuthenticationPrincipal LoginMember member) {
        return ResponseEntity.ok(service.findFavorites(member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(
        @AuthenticationPrincipal LoginMember member, @PathVariable long id) {
        service.deleteFavorite(id, member);
        return ResponseEntity.noContent().build();
    }
}
