package net.mindlevel.client.services;

import net.mindlevel.shared.Captcha;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>CaptchaService</code>.
 */
public interface CaptchaServiceAsync {
    void verify(String solution, String captchaToken, AsyncCallback<Boolean> callback)
            throws IllegalArgumentException;
    void get(AsyncCallback<Captcha> callback)
            throws IllegalArgumentException;
}
