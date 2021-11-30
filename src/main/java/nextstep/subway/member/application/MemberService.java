package nextstep.subway.member.application;

import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
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
        return MemberResponse.of(member(id));
    }

    public void updateMember(Long id, MemberRequest param) {
        member(id).update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private Member member(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(String.format("회원 id(%d)가 존재하지 않습니다.", id)));
    }
}
