package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.StringAuthPrincipal;
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

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<URI> createFavorite(@StringAuthPrincipal LoginMember loginMember,
                                              @RequestBody FavoriteRequest favoriteRequest) {

        Long id = favoriteService.save(loginMember.getId(),
                                       favoriteRequest.getSource(),
                                       favoriteRequest.getTarget());

        return ResponseEntity.created(URI.create("/favorites" + id)).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findFavorite(@StringAuthPrincipal LoginMember loginMember) {

        List<FavoriteResponse> responses = favoriteService.findAllByMember(loginMember.getId())
                                                          .stream()
                                                          .map(FavoriteResponse::of)
                                                          .collect(toList());

        return ResponseEntity.ok().body(responses);
    }

    @DeleteMapping(value = "/{favoriteId}")
    public ResponseEntity<Void> deleteFavorite(@StringAuthPrincipal LoginMember loginMember,
                                               @PathVariable Long favoriteId) {
        favoriteService.delete(loginMember.getId(), favoriteId);
        return ResponseEntity.noContent().build();
    }
}
