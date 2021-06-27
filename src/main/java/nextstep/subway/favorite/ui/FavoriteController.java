package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.favorite.application.FavoriteService;

import javax.validation.Valid;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity add(@AuthenticationPrincipal LoginMember loginMember,
                              @Valid @RequestBody FavoriteRequest favoriteRequest) {
        favoriteService.add(loginMember, favoriteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity search(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.search(loginMember));
    }

    @DeleteMapping
    public ResponseEntity remove(@AuthenticationPrincipal LoginMember loginMember) {
        favoriteService.remove(loginMember);
        return ResponseEntity.noContent().build();
    }
}
