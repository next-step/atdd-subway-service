package nextstep.subway.member.application;

import nextstep.subway.error.SubwayInternalException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMemberResponse(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new SubwayInternalException("사용자가 없습니다."));
        return MemberResponse.of(member);
    }

    public Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new SubwayInternalException("사용자가 없습니다."));
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new SubwayInternalException("사용자가 없습니다."));
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

}
