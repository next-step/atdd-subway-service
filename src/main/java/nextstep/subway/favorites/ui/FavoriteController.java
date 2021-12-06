package nextstep.subway.favorites.ui;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.dto.FavoriteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName : nextstep.subway.favorites.dto.ui
 * fileName : FavoriteController
 * author : haedoang
 * date : 2021/12/06
 * description :
 */
@RequestMapping("/favorites")
@RestController
@RequiredArgsConstructor
public class FavoriteController {

    @PostMapping
    public ResponseEntity<Void> saveFavorites(@AuthenticationPrincipal LoginMember member, @RequestBody FavoriteRequest request) {


        System.out.println(member);
        System.out.println(request);

        return ResponseEntity.ok().build();
    }
}
