package nextstep.subway.member.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

@Service
public class MemberService {
    private static final String MESSAGE_MEMBER_ENTITY_NOT_FOUND = "회원이 존재하지 않습니다";
    private static final String MESSAGE_MEMBER_ID_IS_ILLEGAL = "회원이 존재하지 않습니다";
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberEntity(id));
    }

    public Member findMemberEntity(Long id) {
        checkIdIsNull(id);
        return memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MESSAGE_MEMBER_ENTITY_NOT_FOUND));
    }

    private void checkIdIsNull(Long id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException(MESSAGE_MEMBER_ID_IS_ILLEGAL);
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
