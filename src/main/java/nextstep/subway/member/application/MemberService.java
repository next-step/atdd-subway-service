package nextstep.subway.member.application;

import org.springframework.stereotype.Service;

import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;

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
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		return MemberResponse.of(member);
	}

	public void updateMember(Long id, MemberRequest param) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);
		member.update(param.toMember());
	}

	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	public Member getById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "member.id:{}", id));
	}
}
