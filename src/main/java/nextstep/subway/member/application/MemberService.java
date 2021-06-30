package nextstep.subway.member.application;

import nextstep.subway.exception.CustomException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

import static nextstep.subway.exception.CustomExceptionMessage.NOT_FOUND_MEMBER;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        Member member = findMemberById(id);
        return MemberResponse.of(member);
    }

    public MemberResponse updateMember(Long id, MemberRequest param) {
        Member member = findMemberById(id);
        member.update(param.toMember());
        return MemberResponse.of(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                               .orElseThrow(() -> new CustomException(NOT_FOUND_MEMBER));
    }
}
