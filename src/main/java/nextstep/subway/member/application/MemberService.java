package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.from(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findById(id);
        return MemberResponse.from(member);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = findById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다. id: " + id));
    }
}
