package net.basicmodel.sendmail;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * Copyright (C) 2021,2021/5/21, a Tencent company. All rights reserved.
 * <p>
 * User : v_xhangxie
 * <p>
 * Desc :
 */
public class MyAuthenticator extends Authenticator {
    private final String strUser;
    private final String strPwd;

    public MyAuthenticator(String user, String password) {
        this.strUser = user;
        this.strPwd = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(strUser, strPwd);
    }
}
