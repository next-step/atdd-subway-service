package nextstep.subway.member.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.member.application.exception.MemberErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.infrastructure.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> NotFoundException.of(MemberErrorCode.MEMBER_NOTFOUND));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> NotFoundException.of(MemberErrorCode.MEMBER_NOTFOUND));
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> NotFoundException.of(MemberErrorCode.MEMBER_NOTFOUND));
    }
}
