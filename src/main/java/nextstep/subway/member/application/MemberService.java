package nextstep.subway.member.application;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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
        Member member;

        try {
            member = memberRepository.save(request.toMember());
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("중복된 사용자 등록 요청 입니다.");
        }

        return MemberResponse.of(member);
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("멤버를 찾을 수 없습니다."));
        return MemberResponse.of(member);
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("멤버를 찾을 수 없습니다."));

        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
