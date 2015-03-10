package com.javapapers.java.social.facebook;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;

public class MainMenu extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String code="";
	public static CouchbaseCluster cluster = null;
	static String b = "AKOSHA";
	public static Bucket bucket = null;

	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {		
		code = req.getParameter("code");
		if (code == null || code.equals("")) {
			throw new RuntimeException(
					"ERROR: Didn't get code parameter in callback.");
		}
		
		
		FBConnection fbConnection = new FBConnection();
		String accessToken = null;
		try {
			accessToken = fbConnection.getAccessToken(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FBGraph fbGraph = new FBGraph(accessToken);
		String graph = null;
		
		
		try {
			graph = fbGraph.getFBGraph();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Map<String, String> fbProfileData = null;
		try {
			fbProfileData = fbGraph.getGraphData(graph);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cluster = CouchbaseCluster.create("127.0.0.1:8080");
		bucket = cluster.openBucket(b);
		
		ServletOutputStream out = res.getOutputStream();
		out.println("<h1>FaceBook Fetching!!!</h1>");
		out.println("<h2>Just Do It !!</h2>");
		
		JsonObject obj = JsonObject.create();
		
		out.println("POSTS:");
		for ( String s : fbProfileData.keySet()) {
			if ( bucket.get(s) == null ) {
				System.out.println("Some THing Wrong happened");
			}else {
				out.println(bucket.get(s).content().toString());
			}
		}	
	}

}
