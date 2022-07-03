package nextstep.subway.favorite.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class FavoriteController {
    private final FavoriteService favoriteService;

    private final StationService stationService;

    private final MemberService memberService;


    public FavoriteController(FavoriteService favoriteService, StationService stationService, MemberService memberService) {
        this.favoriteService = favoriteService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @PostMapping("/favorites")
    public ResponseEntity<Void> createFavorite(@AuthenticationPrincipal LoginMember loginMember, @RequestBody FavoriteRequest request) {
        Member member = memberService.findMemberById(loginMember.getId());
        Station sourceStation = stationService.findStationById(request.getSource());
        Station targetStation = stationService.findStationById(request.getTarget());

        FavoriteResponse response = favoriteService.createFavorite(member, sourceStation, targetStation);
        return ResponseEntity.created(URI.create("/favorites/" + response.getId())).build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> findMyFavorites(@AuthenticationPrincipal LoginMember loginMember) {
        return ResponseEntity.ok(favoriteService.findMyFavorites(loginMember));
    }

    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long id) {
        favoriteService.removeFavorite(loginMember, id);
        return ResponseEntity.ok().build();
    }
}
