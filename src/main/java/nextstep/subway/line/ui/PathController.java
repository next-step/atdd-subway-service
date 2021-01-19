package nextstep.subway.line.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.CostType;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Stations;

import static java.util.Arrays.*;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paths")
public class PathController {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberService memberService;

    public PathController(final LineService lineService, final StationService stationService, final MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@AuthenticationPrincipal LoginMember loginMember, @RequestParam("source") Long sourceId, @RequestParam("target") Long targetId) {
        Stations stations = new Stations(stationService.findByIds(asList(sourceId, targetId)));
        PathFinder pathFinder = new PathFinder(lineService.findAllLines());
        CostType costType = CostType.getCostType(memberService.findMember(loginMember.getId()).getAge());
        return ResponseEntity.ok(pathFinder.findPath(costType, stations.getStation(sourceId), stations.getStation(targetId)));
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity<String> handleIllegalException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
