package nextstep.subway.common.exception;

/**
 * @author : byungkyu
 * @date : 2021/01/15
 * @description :
 **/
public class NoFavoriteExistException extends BaseException {
	public static String ERROR_CODE = "NO_FAVORITE_EXIST_EXCEPTION";

	public static String ERROR_MESSAGE = "즐겨찾기가 존재하지 않습니다.";

	public NoFavoriteExistException() {
	}

	public String getErrorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getErrorMessage() {
		return ERROR_MESSAGE;
	}

}