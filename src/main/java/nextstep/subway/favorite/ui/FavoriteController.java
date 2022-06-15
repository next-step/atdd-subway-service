package nextstep.subway.favorite.ui;

import java.net.URI;
import java.util.List;
import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.service.FavoriteService;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.PathService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
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
    private final PathService pathService;
    private final StationService stationService;
    private final LineService lineService;
    private final MemberService memberService;

    public FavoriteController(FavoriteService favoriteService, PathService pathService,
        StationService stationService, LineService lineService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.pathService = pathService;
        this.stationService = stationService;
        this.lineService = lineService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavoriteList(
        @AuthenticationPrincipal LoginMember loginMember) {
        Member member = memberService.findById(loginMember.getId());
        List<FavoriteResponse> favoriteList = favoriteService.getFavoriteList(member);
        return ResponseEntity.ok().body(favoriteList);
    }

    @PostMapping
    public ResponseEntity saveFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @RequestBody FavoriteRequest favoriteRequest) {
        Member member = memberService.findById(loginMember.getId());
        Station sourceStation = stationService.findById(favoriteRequest.getSource());
        Station destStation = stationService.findById(favoriteRequest.getTarget());
        List<Line> lines = lineService.findAllLines();

        PathResponse path = pathService.findPath(sourceStation, destStation, lines);

        Favorite favorite = favoriteService.saveFavorite(member, sourceStation, destStation);
        return ResponseEntity.created(URI.create("/favorites/" + favorite.getId())).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFavorite(@AuthenticationPrincipal LoginMember loginMember,
        @PathVariable Long id) {
        Member member = memberService.findById(loginMember.getId());
        favoriteService.deleteFavorite(id, member);
        return ResponseEntity.noContent().build();
    }
}
