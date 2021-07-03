package nextstep.subway.member.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.IncompleteLoginMember;
import nextstep.subway.member.application.MemberCommandService;
import nextstep.subway.member.application.MemberQueryService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class MemberController {
    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

    public MemberController(MemberQueryService memberQueryService, MemberCommandService memberCommandService) {
        this.memberQueryService = memberQueryService;
        this.memberCommandService = memberCommandService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberRequest request) {
        MemberResponse member = memberCommandService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + member.getId())).build();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberResponse> findMember(@PathVariable Long id) {
        MemberResponse member = memberQueryService.findMemberResponseById(id);
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberRequest param) {
        memberCommandService.updateMember(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<MemberResponse> deleteMember(@PathVariable Long id) {
        memberCommandService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MemberResponse> findMemberOfMine(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember) {
        MemberResponse member = memberQueryService.findMemberResponseById(incompleteLoginMember.toCompleteLoginMember().getId());
        return ResponseEntity.ok().body(member);
    }

    @PutMapping("/members/me")
    public ResponseEntity<MemberResponse> updateMemberOfMine(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember, @RequestBody MemberRequest param) {
        memberCommandService.updateMember(incompleteLoginMember.toCompleteLoginMember().getId(), param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/me")
    public ResponseEntity<MemberResponse> deleteMemberOfMine(@AuthenticationPrincipal IncompleteLoginMember incompleteLoginMember) {
        memberCommandService.deleteMember(incompleteLoginMember.toCompleteLoginMember().getId());
        return ResponseEntity.noContent().build();
    }
}
