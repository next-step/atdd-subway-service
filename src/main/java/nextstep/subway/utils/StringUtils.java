package nextstep.subway.utils;

import nextstep.subway.enums.ErrorMessage;

public class StringUtils {

    public static boolean isNullOrEmpty(String text){
        return text == null || text.trim().isEmpty();
    }

    public static Long StringToLong(String text){
        try{
            return Long.parseLong(text);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException(ErrorMessage.INVALID_STRING_FORMAT.getMessage());
        }
    }
}
