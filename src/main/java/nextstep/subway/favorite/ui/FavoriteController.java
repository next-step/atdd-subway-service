package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final MemberService memberService;

    public FavoriteController(final FavoriteService favoriteService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.memberService = memberService;
    }

    @PostMapping("")
    public ResponseEntity<FavoriteResponse> createLine(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest favoriteRequest) {
        final Member member = memberService.findMemberById(loginMember.getId());
        final FavoriteResponse favoriteResponse = favoriteService.saveFavorite(member, favoriteRequest);
        return ResponseEntity.created(URI.create("/favorites/" + favoriteResponse.getId())).body(favoriteResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<FavoriteResponse> findLineById(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        final Member member = memberService.findMemberById(loginMember.getId());
        return ResponseEntity.ok(favoriteService.findLineResponseById(member, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteLine(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        final Member member = memberService.findMemberById(loginMember.getId());
        favoriteService.deleteLineById(member, id);
        return ResponseEntity.noContent().build();
    }
}
