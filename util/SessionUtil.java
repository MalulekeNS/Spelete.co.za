package com.spelete.shop.util;
import jakarta.servlet.http.*; 
public class SessionUtil {
  public static final String USER_ID_ATTR="USER_ID"; public static final String USER_NAME_ATTR="USER_NAME"; public static final String USER_EMAIL_ATTR="USER_EMAIL";
  public static HttpSession session(HttpServletRequest req){ return req.getSession(true); }
}
