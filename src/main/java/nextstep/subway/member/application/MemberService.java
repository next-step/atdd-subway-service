package nextstep.subway.member.application;

import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMember(Long id) {
		Member member = findMemberById(id);
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest memberRequest) {
		Member member = findMemberById(id);
		member.update(memberRequest.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member findByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new AuthorizationException("해당 이메일을 사용하는 회원은 존재하지 않습니다."));
	}

	private Member findMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
	}
}
