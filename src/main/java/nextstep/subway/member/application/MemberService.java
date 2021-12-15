package nextstep.subway.member.application;

import java.util.NoSuchElementException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponse createMember(final MemberRequest request) {
        final Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(final Long id) {
        return MemberResponse.of(
            findMemberById(id)
        );
    }

    @Transactional(readOnly = true)
    public Member findMemberById(final Long id) {
        return memberRepository.findById(id)
            .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateMember(final Long id, final MemberRequest param) {
        final Member member = findMemberById(id);
        member.update(param.toMember());
    }

    @Transactional
    public void deleteMember(final Long id) {
        final Member member = findMemberById(id);
        memberRepository.delete(member);
    }
}
