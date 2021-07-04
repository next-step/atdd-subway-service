package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.IncompleteLoginMember;
import nextstep.subway.favorite.application.FavoriteCommandService;
import nextstep.subway.favorite.application.FavoriteQueryService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteQueryService favoriteQueryService;
    private final FavoriteCommandService favoriteCommandService;

    public FavoriteController(FavoriteQueryService favoriteQueryService, FavoriteCommandService favoriteCommandService) {
        this.favoriteQueryService = favoriteQueryService;
        this.favoriteCommandService = favoriteCommandService;
    }

    @PostMapping
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember, @RequestBody FavoriteRequest request) {
        FavoriteResponse favorite = favoriteCommandService.createFavorite(incompleteLoginMember.toCompleteLoginMember(), request);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorites(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember) {
        return ResponseEntity.ok(favoriteQueryService.findFavoriteResponsesByMemberId(incompleteLoginMember.toCompleteLoginMember()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember, @PathVariable Long id) {
        favoriteCommandService.deleteByIdAndLoginMember(id, incompleteLoginMember.toCompleteLoginMember());
        return ResponseEntity.noContent().build();
    }
}
