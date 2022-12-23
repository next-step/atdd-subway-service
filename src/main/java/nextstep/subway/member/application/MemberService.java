package nextstep.subway.member.application;

import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
        return MemberResponse.of(findMemberEntity(id));
    }

    public Member findMemberEntity(Long id) {
        checkIdIsNull(id);
        return memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
    }

    private void checkIdIsNull(Long id) {
        if (Objects.isNull(id)) {
            throw new EntityNotFoundException("회원이 존재하지 않습니다");
        }
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("회원 정보를 찾을 수 없습니다." + id))
            );
    }

    public void updateMember(Long id, MemberRequest param) {
        Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
        member.update(param.toMember());
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
