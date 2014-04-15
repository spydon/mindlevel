package net.mindlevel.client.services;

import net.mindlevel.shared.Captcha;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("captcha")
public interface CaptchaService extends RemoteService {
    boolean verify(String solution, String captchaToken) throws IllegalArgumentException;
    Captcha get() throws IllegalArgumentException;
}
