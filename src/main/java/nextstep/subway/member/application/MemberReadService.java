package nextstep.subway.member.application;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import nextstep.subway.member.domain.*;
import nextstep.subway.member.dto.*;

@Service
@Transactional(readOnly = true)
public class MemberReadService {
    private final MemberRepository memberRepository;

    public MemberReadService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        return MemberResponse.of(member);
    }
}
