package nextstep.subway.domain.member.application;

import nextstep.subway.domain.auth.domain.LoginMember;
import nextstep.subway.domain.member.domain.Member;
import nextstep.subway.domain.member.domain.MemberRepository;
import nextstep.subway.domain.member.dto.MemberRequest;
import nextstep.subway.domain.member.dto.MemberResponse;
import nextstep.subway.global.exception.EntityNotFoundException;
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
    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(EntityNotFoundException::new);
    }

    public MemberResponse updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        member.update(param.toMember());
        return MemberResponse.of(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
