package nextstep.subway.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public MemberResponse createMember(MemberRequest request) {
		Member member = memberRepository.save(request.toMember());
		return MemberResponse.of(member);
	}

	public MemberResponse findMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return MemberResponse.of(member);
	}

	@Transactional
	public void updateMember(Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.toMember());
	}

	@Transactional
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member findById(Long id) {
		return memberRepository.findById(id).orElseThrow(RuntimeException::new);
	}
}
