//package net.mindlevel.shared;
//
//import java.io.Serializable;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//
///**
// * This class represents an access token returned by the Facebook Login service, along with associated
// * metadata such as its expiration date and permissions. In general, the {@link Session} class will
// * abstract away the need to worry about the details of an access token, but there are situations
// * (such as handling native links, importing previously-obtained access tokens, etc.) where it is
// * useful to deal with access tokens directly. Factory methods are provided to construct access tokens.
// * <p/>
// * For more information on access tokens, see
// * https://developers.facebook.com/docs/concepts/login/access-tokens-and-types/.
// */
//public final class AccessToken implements Serializable {
//    private static final long serialVersionUID = 1L;
//    static final String ACCESS_TOKEN_KEY = "access_token";
//    static final String EXPIRES_IN_KEY = "expires_in";
//    private static final Date MIN_DATE = new Date(Long.MIN_VALUE);
//    private static final Date MAX_DATE = new Date(Long.MAX_VALUE);
//    private static final Date DEFAULT_EXPIRATION_TIME = MAX_DATE;
//    private static final Date DEFAULT_LAST_REFRESH_TIME = new Date();
//    private static final Date ALREADY_EXPIRED_EXPIRATION_TIME = MIN_DATE;
//
//    private final Date expires;
//    private final List<String> permissions;
//    private final String token;
//    private final Date lastRefresh;
//
//    AccessToken(Date expires, List<String> permissions) {
//        if (permissions == null) {
//            permissions = Collections.emptyList();
//        }
//
//        this.expires = expires;
//        this.permissions = Collections.unmodifiableList(permissions);
//        this.token = generateToken();
//        this.lastRefresh = new Date();
//    }
//    
//    private String generateToken() {
//    	try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//    	String token = "";
//    	return token;
//    }
//
//    /**
//     * Gets the string representing the access token.
//     *
//     * @return the string representing the access token
//     */
//    public String getToken() {
//        return this.token;
//    }
//
//    /**
//     * Gets the date at which the access token expires.
//     *
//     * @return the expiration date of the token
//     */
//    public Date getExpires() {
//        return this.expires;
//    }
//
//    /**
//     * Gets the list of permissions associated with this access token. Note that the most up-to-date
//     * list of permissions is maintained by the Facebook service, so this list may be outdated if
//     * permissions have been added or removed since the time the AccessToken object was created. For
//     * more information on permissions, see https://developers.facebook.com/docs/reference/login/#permissions.
//     *
//     * @return a read-only list of strings representing the permissions granted via this access token
//     */
//    public List<String> getPermissions() {
//        return this.permissions;
//    }
//
//    /**
//     * Gets the date at which the token was last refreshed. Since tokens expire, the Facebook SDK
//     * will attempt to renew them periodically.
//     *
//     * @return the date at which this token was last refreshed
//     */
//    public Date getLastRefresh() {
//        return this.lastRefresh;
//    }
//
//    /**
//     * Creates a new AccessToken using the supplied information from a previously-obtained access
//     * token (for instance, from an already-cached access token obtained prior to integration with the
//     * Facebook SDK).
//     *
//     * @param accessToken       the access token string obtained from Facebook
//     * @param expirationTime    the expiration date associated with the token; if null, an infinite expiration time is
//     *                          assumed (but will become correct when the token is refreshed)
//     * @param lastRefreshTime   the last time the token was refreshed (or when it was first obtained); if null,
//     *                          the current time is used.
//     * @param accessTokenSource an enum indicating how the token was originally obtained (in most cases,
//     *                          this will be either AccessTokenSource.FACEBOOK_APPLICATION or
//     *                          AccessTokenSource.WEB_VIEW); if null, FACEBOOK_APPLICATION is assumed.
//     * @param permissions       the permissions that were requested when the token was obtained (or when
//     *                          it was last reauthorized); may be null if permission set is unknown
//     * @return a new AccessToken
//     */
//    public static AccessToken createFromExistingAccessToken(String accessToken, Date expirationTime,
//            Date lastRefreshTime, List<String> permissions) {
//        if (expirationTime == null) {
//            expirationTime = DEFAULT_EXPIRATION_TIME;
//        }
//        if (lastRefreshTime == null) {
//            lastRefreshTime = DEFAULT_LAST_REFRESH_TIME;
//        }
//
//        return new AccessToken(accessToken, expirationTime, permissions, lastRefreshTime);
//    }
//
//    /**
//     * Creates a new AccessToken using the information contained in an Intent populated by the Facebook
//     * application in order to launch a native link. For more information on native linking, please see
//     * https://developers.facebook.com/docs/mobile/android/deep_linking/.
//     *
//     * @param intent the Intent that was used to start an Activity; must not be null
//     * @return a new AccessToken, or null if the Intent did not contain enough data to create one
//     */
//
//    static AccessToken createEmptyToken(List<String> permissions) {
//        return new AccessToken("", ALREADY_EXPIRED_EXPIRATION_TIME, permissions, DEFAULT_LAST_REFRESH_TIME);
//    }
//
//    static AccessToken createFromString(String token, List<String> permissions) {
//        return new AccessToken(token, DEFAULT_EXPIRATION_TIME, permissions, DEFAULT_LAST_REFRESH_TIME);
//    }
//}