package nextstep.subway.member.application;

import nextstep.subway.error.exception.IllegalRequestException;
import nextstep.subway.error.exception.NotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {
  private final MemberRepository memberRepository;

  public MemberService(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Transactional
  public MemberResponse createMember(MemberRequest request) {
    Member member = memberRepository.save(request.toMember());
    return MemberResponse.of(member);
  }

  public MemberResponse findMember(Long id) {
    Member member = findMemberById(id);
    return MemberResponse.of(member);
  }

  @Transactional
  public void updateMember(Long id, MemberRequest param) {
    Member member = findMemberById(id);
    member.update(param.toMember());
  }

  @Transactional
  public void deleteMember(Long id) {
    memberRepository.deleteById(id);
  }

  private Member findMemberById(Long id) {
    checkIdNull(id);
    return memberRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
  }

  private void checkIdNull(Long id) {
    if (id == null) {
      throw new IllegalRequestException("로그인 유저 정보가 올바르지 않습니다.");
    }
  }
}
