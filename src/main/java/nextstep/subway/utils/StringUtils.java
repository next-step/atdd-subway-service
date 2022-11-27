package nextstep.subway.utils;

import nextstep.subway.common.constant.ErrorCode;

public class StringUtils {

    public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static Long stringToLong(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException ne) {
            throw new IllegalArgumentException(ErrorCode.숫자가_될_수_있는_문자열.getErrorMessage());
        }
    }
}
