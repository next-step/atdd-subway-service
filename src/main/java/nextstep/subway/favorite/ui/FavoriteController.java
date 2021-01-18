package nextstep.subway.favorite.ui;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    public FavoriteController() {
    }

    @PostMapping
    public ResponseEntity createMember(@RequestBody FavoriteRequest request) {
        return ResponseEntity.created(URI.create("/favorite/1")).build();
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> findAllFavorites() {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FavoriteResponse> deleteFavorite(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }
}