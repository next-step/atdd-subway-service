package nextstep.subway.member.application;

import nextstep.subway.exception.NotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMemberResponse(Long id) {
        Member member = findMember(id);
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = findMember(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        Member member = findMember(id);
        memberRepository.delete(member);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
