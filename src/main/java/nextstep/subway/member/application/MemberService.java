package nextstep.subway.member.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        checkIdIsNull(id);
        Member member = memberRepository.findById(id).orElseThrow(AuthorizationException::new);
        return MemberResponse.of(member);
    }

    private static void checkIdIsNull(Long id) {
        if (Objects.isNull(id)) {
            throw new AuthorizationException();
        }
    }

    public void updateMember(Long id, MemberRequest param) {
        checkIdIsNull(id);
        Member member = memberRepository.findById(id).orElseThrow(AuthorizationException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        checkIdIsNull(id);
        memberRepository.deleteById(id);
    }
}
