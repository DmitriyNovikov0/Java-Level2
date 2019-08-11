package leson7.chat.common;

public class Library {
    /*
     * /auth_request§login§password
     * /auth_accept§nickname
     * /auth_error
     * /msg_format_error
     * /broadcast§msg
     * */
    public static final String DELIMITER = "§";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_DENIED = "/auth_denied";
    // если мы вдруг не поняли, что за сообщение и не смогли разобрать
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";
    // то есть сообщение, которое будет посылаться всем
    public static final String TYPE_BROADCAST = "/bcast";
    // /user_list§u1§u2§u3§u4
    public static final String USER_LIST = "/user_list";
    public static final String CLIENT_BCAST = "/cl_bcast";

    public static String getUserList(String users) {
        return USER_LIST + DELIMITER + users;
    }

    public static String getAuthRequest(String login, String password) {
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getAuthAccept(String nickname) {
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied() {
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message) {
        return MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getTypeBroadcast(String src, String message) {
        return TYPE_BROADCAST + DELIMITER + System.currentTimeMillis() +
                DELIMITER + src + DELIMITER + message;
    }

    public static String getClientBcast(String msg) {
        return CLIENT_BCAST + DELIMITER + msg;
    }
}
