package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
        if (id == null) {
            throw new IllegalArgumentException("회원번호가 입력되지 않았습니다.");
        }
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return MemberResponse.of(member);
    }

    public Member findMemberById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("회원번호가 입력되지 않았습니다.");
        }
        return memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
