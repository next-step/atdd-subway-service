package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
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
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<FavoriteResponse> createFavorite(
            @AuthenticationPrincipal Member member,
            @RequestBody FavoriteRequest favoriteRequest
    ){
        FavoriteResponse favorite = favoriteService.createFavorite(member.getId(), favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal Member member){
        List<FavoriteResponse> favorites = favoriteService.getFavorites(member.getId());
        return ResponseEntity.ok().body(favorites);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(
            @AuthenticationPrincipal Member member,
            @PathVariable Long id
    ){
        favoriteService.deleteFavorite(member.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
