package nextstep.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findMemberFromRepository(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findMemberFromRepository(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private Member findMemberFromRepository(Long id) {
        return memberRepository.findById(id).orElseThrow(Exceptions.MEMBER_NOT_FOUND::getException);
    }
}
