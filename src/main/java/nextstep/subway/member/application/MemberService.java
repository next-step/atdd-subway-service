package nextstep.subway.member.application;

import nextstep.subway.exception.InvalidRequestException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest request) {
        try {
            return MemberResponse.of(saveMemberByRequest(request));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("중복된 사용자 등록 요청 입니다.");
        }
    }

    @Transactional(readOnly = true)
    public MemberResponse findMember(Long id) {
        return MemberResponse.of(findMemberById(id));
    }

    public void updateMember(Long id, MemberRequest param) {
        Optional<Member> duplicateMember = memberRepository.findByEmail(param.getEmail());
        if (duplicateMember.isPresent()) {
            throw new InvalidRequestException("이메일이 중복된 계정이 존재합니다.");
        }

        Member member = findMemberById(id);

        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    private Member saveMemberByRequest(MemberRequest request) {
        return memberRepository.save(request.toMember());
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("멤버를 찾을 수 없습니다."));
    }
}
