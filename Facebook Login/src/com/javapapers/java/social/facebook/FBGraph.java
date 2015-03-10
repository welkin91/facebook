package com.javapapers.java.social.facebook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class FBGraph {
	private String accessToken;
	public static CouchbaseCluster cluster = null;
	static String b = "AKOSHA";
	public static Bucket bucket = null;

	public FBGraph(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getFBGraph() throws Exception {
		String graph = null;
		String g = "https://graph.facebook.com/me?" + accessToken;
	
		URL u = new URL(g);
		URLConnection c = u.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				c.getInputStream()));
		String inputLine;
		StringBuffer b = new StringBuffer();
		while ((inputLine = in.readLine()) != null)
			b.append(inputLine + "\n");
		in.close();
		graph = b.toString();
		System.out.println(graph);
		
		return graph;
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						// Making post msg as key makes it easy to find and check for exact match.
						// Further NLP can also be used to check if two strings have same context or not
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Map<String, String> getGraphData(String fbGraph) throws Exception {
		
		JsonObject obj = JsonObject.create();
		Map<String,String> fbp = new HashMap<String,String>();
		Map<String, JsonObject> fbProfile = new HashMap<String, JsonObject>();
		String ID = "";
		
		JSONObject json = new JSONObject(fbGraph);
		obj.put("id", json.getString("id"));
		obj.put("first_name", json.getString("first_name"));
		if (json.has("email")){
			obj.put("email", json.getString("email"));
		}
		obj.put("post", json.getString("post"));
		
		fbProfile.put(json.getString("id"), obj);
		fbp.put(json.getString("id"), obj.getString(ID));
		
		cluster = CouchbaseCluster.create("127.0.0.1:8080");
		bucket = cluster.openBucket(b);
		
		ID = URLEncoder.encode(obj.getString("post"), "UTF-8");
		
		if ( bucket.get(ID) != null ) 
			bucket.upsert(JsonDocument.create(ID, obj));
		else 	System.out.println("Duplicate Element!!\nalready present in databse");
		
		return fbp;
	}

}
