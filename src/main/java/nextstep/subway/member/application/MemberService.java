package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.exception.MemberException;
import nextstep.subway.member.exception.MemberExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(final MemberRequest request) {
        final Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(final Long id) {
        final Member member = findById(id);
        return MemberResponse.of(member);
    }

    public void updateMember(final Long id, final MemberRequest param) {
        final Member member = findById(id);
        member.update(param.toMember());
    }

    public void deleteMember(final Long id) {
        memberRepository.deleteById(id);
    }

    private Member findById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_EXISTS_USER));
    }
}
