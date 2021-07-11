package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
	@DisplayName("즐겨찾기 관리 시나리오")
	@Test
	void manageFavorite() {
		// Background
		// Given : 지하철역 등록되어 있음
		// And : 지하철 노선 등록되어 있음
		// And : 지하철 노선에 지하철역 등록되어 있음
		// And : 회원 등록되어 있음
		// And : 로그인 되어 있음
		// Scenario : 즐겨찾기 관리
		// When : 즐겨찾기 생성 요청
		// Then : 즐겨찾기 생성됨
		// When : 즐겨찾기 목록 조회 요청
		// Then : 즐겨찾기 목록 조회됨
		// When : 즐겨찾기 삭제 요청
		// then : 즐겨찾기 삭제됨
	}

	@DisplayName("즐겨찾기 관리 에러 시나리오")
	@Test
	void manageFavoriteError() {
		// Background
		// Given : 지하철역 / 노선 / 회원 등록 / 로그인 되어 있지 않음
		// Scenario 즐겨찾기 관리 에러
		// When : 즐겨찾기 생성 요청
		// Then : 즐겨찾기 생성 실패
		// Given : 지하철역, 노선 생성
		// When : 즐겨찾기 생성 요청
		// Then : 즐겨찾기 생성 실패
		// Given : 회원 등록
		// When : 즐겨찾기 생성 요청
		// Then : 즐겨찾기 생성 실패
		// Given : 미로그인 상태
		// When : 즐겨찾기 목록 조회 요청
		// Then : 즐겨찾기 목록 조회 실패
		// When : 즐겨찾기 삭제 요청
		// Then : 즐겨찾기 삭제 실패
	}
}
