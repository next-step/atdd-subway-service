package nextstep.subway.member.application;

import nextstep.subway.member.application.exception.DuplicateEmailException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberCommandService {
    public static final String DUPLICATED_EMAIL_EXCEPTION_MESSAGE = "등록된 Email이 있습니다.";

    private final MemberQueryService memberQueryService;
    private final MemberRepository memberRepository;

    public MemberCommandService(MemberQueryService memberQueryService, MemberRepository memberRepository) {
        this.memberQueryService = memberQueryService;
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        validateDuplicatedEmail(request.getEmail());
        Member member = memberRepository.save(request.toMember());
        return MemberResponse.of(member);
    }

    private void validateDuplicatedEmail(String email) {
        if (memberQueryService.existsByEmail(email)) {
            throw new DuplicateEmailException(DUPLICATED_EMAIL_EXCEPTION_MESSAGE);
        }
    }

    public void updateMember(Long id, MemberRequest param) {
        validateDuplicatedEmail(param.getEmail());
        Member member = memberQueryService.findMemberById(id);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
