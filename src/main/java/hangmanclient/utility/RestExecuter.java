package hangmanclient.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

public class RestExecuter {
    private Logger logger = Logger.getLogger(RestExecuter.class.getName());
    private Gson gson;
    private Header jsonHeader = new BasicHeader("Content-Type","application/json");

    public RestExecuter() {
        this.gson = new Gson();
    }

    private String url = "http://localhost:8090/";

    public String executePost(String endpoint, Object content){
        final String query = url + endpoint;
        HttpPost httpPost = new HttpPost(query);
        httpPost.addHeader(jsonHeader);
        StringEntity params;

        try {
            String jsonContent = gson.toJson(content);
            logger.log(Level.INFO,"[JSON content]: {0}" + jsonContent);
            params = new StringEntity(jsonContent);
            httpPost.setEntity(params);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE,"Unable to process to json: {0}", e);
        }

        return executeHttpUriRequest(httpPost);
    }

    public String executeGet(String endpoint, String content){
        final String query = url + endpoint + content;
        HttpGet httpGet = new HttpGet(query);
        return executeHttpUriRequest(httpGet);
    }

    public String executeGet(String endpoint){
        final String query = url + endpoint;
        HttpGet httpGet = new HttpGet(query);
        return executeHttpUriRequest(httpGet);
    }

    public String executePut(String endpoint, Object content){
        final String query = url + endpoint;

        HttpPut httpPut = new HttpPut(query);
        httpPut.addHeader(jsonHeader);
        StringEntity params;
        try {
            params = new StringEntity(gson.toJson(content));
            httpPut.setEntity(params);
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.SEVERE,"Unable to process to json: {0}", e);
        }

        return executeHttpUriRequest(httpPut);
    }

    public String executeDelete(String endpoint, String content){
        final String query = url+endpoint+content;
        HttpDelete httpDelete = new HttpDelete(query);
        return executeHttpUriRequest(httpDelete);
    }

    private String executeHttpUriRequest(HttpUriRequest request) {
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(request)) {
            logger.log(Level.INFO,"[Status line] : " + response.getStatusLine());

            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            logger.log(Level.INFO,"[Entity] : " + entityString);

            return entityString;
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Unable to process request: {0}", e);
            return null;
        }

    }
}
