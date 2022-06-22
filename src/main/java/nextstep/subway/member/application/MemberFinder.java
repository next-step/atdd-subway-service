package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class MemberFinder {
    private MemberRepository memberRepository;

    public MemberFinder(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse findMember(Long id) {
        Member member = findById(id);
        return MemberResponse.of(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("멤버를 찾을 수 없습니다."));
    }
}
