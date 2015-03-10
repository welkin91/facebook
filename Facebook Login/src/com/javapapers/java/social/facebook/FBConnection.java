package com.javapapers.java.social.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FBConnection {
	public static final String AppId = "346797038855767";
	public static final String AppSecret = "ad3804334cf2a44396a9c403c791bee5";
	public static final String BaseURL = "http://localhost:8080/Facebook_Login/fbhome";

	static String accessToken = "";

	public String getFBAuthUrl() {
		String fbLoginUrl = "";
		try {
			fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
					+ FBConnection.AppId + "&BaseURL="
					+ URLEncoder.encode(FBConnection.BaseURL, "UTF-8")
					+ "&scope=email";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbLoginUrl;
	}

	public String getFBGraphUrl(String code) {
		String fbGraphUrl = "";
		try {
			fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
					+ "client_id=" + FBConnection.AppId + "&BaseURL="
					+ URLEncoder.encode(FBConnection.BaseURL, "UTF-8")
					+ "&client_secret=" + AppSecret + "&code=" + code;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbGraphUrl;
	}

	public String getAccessToken(String code) throws Exception {
		if ("".equals(accessToken)) {
			URL fbGraphURL;
			fbGraphURL = new URL(getFBGraphUrl(code));
			
			URLConnection fbConnection;
			String res = "";
			StringBuffer b = null;
			
			fbConnection = fbGraphURL.openConnection();
			BufferedReader in;
			System.out.println("checkStart");
			in = new BufferedReader(new InputStreamReader(
					fbConnection.getInputStream()));
			System.out.println("checkEnd");
			String inputLine;
			b = new StringBuffer();
			while ((inputLine = in.readLine()) != null){
				res += inputLine + "\n";
				b.append(inputLine + "\n");
			}
			in.close();
		

			System.out.println("res = " + res);
			
			accessToken = res;
			if (accessToken.startsWith("{")) {
				throw new RuntimeException("ERROR: Access Token Invalid: "
						+ accessToken);
			}
		}
		return accessToken;
	}
}
