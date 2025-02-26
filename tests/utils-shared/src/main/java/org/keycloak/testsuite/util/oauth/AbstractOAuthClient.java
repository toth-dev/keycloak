package org.keycloak.testsuite.util.oauth;

import org.apache.http.impl.client.CloseableHttpClient;
import org.openqa.selenium.WebDriver;

import java.util.Map;

public abstract class AbstractOAuthClient<T> {

    protected String baseUrl;
    protected OAuthClientConfig config;

    protected Map<String, String> customParameters;
    protected String codeChallenge;
    protected String codeChallengeMethod;
    protected String codeVerifier;
    protected String clientSessionState;
    protected String clientSessionHost;
    protected String dpopJkt;
    protected String dpopProof;
    protected String request;
    protected String requestUri;
    protected String claims;
    protected String kcAction;
    protected String uiLocales;
    protected String maxAge;
    protected String prompt;
    protected StateParamProvider state;
    protected String nonce;

    protected HttpClientManager httpClientManager;
    protected WebDriver driver;

    public AbstractOAuthClient(String baseUrl, CloseableHttpClient httpClient, WebDriver webDriver) {
        this.baseUrl = baseUrl;
        this.driver = webDriver;
        this.httpClientManager = new HttpClientManager(httpClient);
    }

    public T client(String clientId) {
        config.client(clientId);
        return (T) this;
    }

    public T client(String clientId, String clientSecret) {
        config.client(clientId, clientSecret);
        return (T) this;
    }

    public String getLoginFormUrl() {
        return new LoginUrlBuilder(this).toString();
    }

    public void openLoginForm() {
        driver.navigate().to(getLoginFormUrl());
    }

    public AuthorizationEndpointResponse doLogin(String username, String password) {
        openLoginForm();
        fillLoginForm(username, password);
        return parseLoginResponse();
    }

    public abstract void fillLoginForm(String username, String password);

    public AuthorizationEndpointResponse parseLoginResponse() {
        return new AuthorizationEndpointResponse(this);
    }

    public PasswordGrantRequest passwordGrantRequest(String username, String password) {
        return new PasswordGrantRequest(username, password, this);
    }

    public AccessTokenResponse doPasswordGrantRequest(String username, String password) {
        return passwordGrantRequest(username, password).send();
    }

    public AccessTokenRequest accessTokenRequest(String code) {
        return new AccessTokenRequest(code, this);
    }

    public AccessTokenResponse doAccessTokenRequest(String code) {
        return accessTokenRequest(code).send();
    }

    public ClientCredentialsGrantRequest clientCredentialsGrantRequest() {
        return new ClientCredentialsGrantRequest(this);
    }

    public AccessTokenResponse doClientCredentialsGrantAccessTokenRequest() {
        return clientCredentialsGrantRequest().send();
    }

    public RefreshRequest refreshRequest(String refreshToken) {
        return new RefreshRequest(refreshToken, this);
    }

    public AccessTokenResponse doRefreshTokenRequest(String refreshToken) {
        return refreshRequest(refreshToken).send();
    }

    public UserInfoRequest userInfoRequest(String accessToken) {
        return new UserInfoRequest(accessToken, this);
    }

    public UserInfoResponse doUserInfoRequest(String accessToken) {
        return userInfoRequest(accessToken).send();
    }

    public TokenRevocationRequest tokenRevocationRequest(String token) {
        return new TokenRevocationRequest(token, this);
    }

    public TokenRevocationResponse doTokenRevoke(String token) {
        return tokenRevocationRequest(token).send();
    }

    public T baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return (T) this;
    }

    public OAuthClientConfig config() {
        return config;
    }

    public T driver(WebDriver webDriver) {
        this.driver = webDriver;
        return (T) this;
    }

    public HttpClientManager httpClient() {
        return httpClientManager;
    }

    public Endpoints getEndpoints() {
        return new Endpoints(baseUrl, config.getRealm());
    }

    public String getRedirectUri() {
        return config.getRedirectUri();
    }

    String getClientSessionState() {
        return clientSessionState;
    }

    String getClientSessionHost() {
        return clientSessionHost;
    }

    String getCodeChallenge() {
        return codeChallenge;
    }

    String getCodeChallengeMethod() {
        return codeChallengeMethod;
    }

    String getCodeVerifier() {
        return codeVerifier;
    }

    Map<String, String> getCustomParameters() {
        return customParameters;
    }

    String getDpopJkt() {
        return dpopJkt;
    }

    String getDpopProof() {
        return dpopProof;
    }

    String getRequestUri() {
        return requestUri;
    }

    String getRequest() {
        return request;
    }

    String getClaims() {
        return claims;
    }

    String getKcAction() {
        return kcAction;
    }

    String getUiLocales() {
        return uiLocales;
    }

    public String getState() {
        return state != null ? state.getState() : null;
    }

    String getNonce() {
        return nonce;
    }

    String getMaxAge() {
        return maxAge;
    }

    String getPrompt() {
        return prompt;
    }

    protected interface StateParamProvider {

        String getState();

    }

}
