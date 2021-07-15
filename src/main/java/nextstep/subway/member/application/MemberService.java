package nextstep.subway.member.application;

import nextstep.subway.advice.exception.MemberBadRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = getMember(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = getMember(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberBadRequestException("존재하지 않는 회원입니다", id));
    }

}
