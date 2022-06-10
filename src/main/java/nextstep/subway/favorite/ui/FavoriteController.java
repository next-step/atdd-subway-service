package nextstep.subway.favorite.ui;

import com.google.common.collect.Lists;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@AuthenticationPrincipal LoginMember loginMember){
        return ResponseEntity.ok(Lists.newArrayList(new FavoriteResponse()));
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> createFavorite(
            @AuthenticationPrincipal LoginMember loginMember
            , @RequestBody FavoriteCreateRequest favoriteCreateRequest
    ) throws URISyntaxException {
        return ResponseEntity.created(new URI("/")).body(new FavoriteResponse());
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@AuthenticationPrincipal LoginMember loginMember){
        return ResponseEntity.noContent().build();
    }

}
