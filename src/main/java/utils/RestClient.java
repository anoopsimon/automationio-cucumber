package utils;


import jdk.jshell.spi.ExecutionControl;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class RestClient
{
private CloseableHttpClient client;
private String baseUrl;

public RestClient(String baseUrl)
{
    this.baseUrl=baseUrl;
    this.client = initializeHttpClient();
}

private CloseableHttpClient initializeHttpClient(){
    try {
        this.client = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    }

    return this.client;
}

    /**
     * Add header to get request
     * @param request
     * @param httpGet
     */
    private HttpGet addHeaderForGet(RestRequest request, HttpGet httpGet)
    {
        for (String key : request.getHeaders().keySet())
        {
            String value = request.getHeaders().get(key);
            httpGet.addHeader(key,value);
        }
        return httpGet;
    }

    private HttpPost addHeaderForPost(RestRequest request, HttpPost httpPost)
    {
        for (String key : request.getHeaders().keySet())
        {
            String value = request.getHeaders().get(key);
            httpPost.addHeader(key,value);
        }
        return httpPost;
    }


    private void addHeaderForPut(RestRequest request,HttpPut httpPut) {
        for (String key : request.getHeaders().keySet())
        {
            String value = request.getHeaders().get(key);
            httpPut.addHeader(key,value);
        }
    }
    /**
     * To format get url with parameters
     * @param
     * @return
     * @throws URISyntaxException
     */
//    private URI formatGetUrl(RestRequest request) throws URISyntaxException
//    {
//        URIBuilder builder = new URIBuilder();
//        //builder.setScheme("http").setHost("www.google.com").setPath("/search");
//
//        for (String key : request.getHeaders().keySet())
//        {
//            String value= request.getHeaders().get(key);
//            builder.setParameter(key, value);
//        }
//
//        URI uri = builder.build();
//
//        return uri;
//    }

    private void addContentType(HttpPost httpPost,DataFormat dataFormat) throws ExecutionControl.NotImplementedException {
        switch (dataFormat)
        {
            case XML:
                throw new ExecutionControl.NotImplementedException("POST with XML is not implemented");
            case JSON:
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");
                break;
        }


    }

    /**
     * Invoke rest API endpoint having PUT method
     * @param request
     * @return
     * @throws Exception
     */
    public RestResponse invokePut(RestRequest request) throws Exception {
        RestResponse restResponse = new RestResponse();
        HttpPut httpPut = new HttpPut(new URI(concatenatePath(this.baseUrl,request.resource)));
        addHeaderForPut(request,httpPut);
        StringEntity entity = new StringEntity(request.getBody());
        httpPut.setEntity(entity);
        CloseableHttpResponse response = getClient().execute(httpPut);

        String content = EntityUtils.toString(response.getEntity());

        /*Construct RestResponse*/
        restResponse.setContent(content);
        restResponse.setStatusCode(response.getStatusLine().getStatusCode());
        response.setHeaders(response.getAllHeaders());
        restResponse.setCloseableHttpResponse(response);

        return restResponse;
    }



    /**
     * To Invoke a Rest API endpoint having POST Method
     * @param request
     * @return
     * @throws Exception
     */
    public RestRequest invokePost(RestRequest request) throws Exception {

        RestResponse restResponse = new RestResponse();
        /*initialize POST client*/
        HttpPost httpPost = new HttpPost(new URI(concatenatePath(this.baseUrl,request.resource)));
        addContentType(httpPost,request.dataFormat);

        StringEntity entity = new StringEntity(request.getBody());
        httpPost.setEntity(entity);
        addHeaderForPost(request,httpPost);

        /*Execute post*/
        CloseableHttpResponse response = getClient().execute(httpPost);
        String responceContent = EntityUtils.toString(response.getEntity());

        /*Construct RestResponse*/
        restResponse.setContent(responceContent);
        restResponse.setStatusCode(response.getStatusLine().getStatusCode());
        restResponse.setHeaders(response.getAllHeaders());
        restResponse.setCloseableHttpResponse(response);

        return null;
    }


    /**
     * To Invoke a Rest API endpoint having GET Method
     * @param request
     * @return RestResponse
     * @throws Exception
     */
    public RestResponse invokeGet(RestRequest request) throws Exception {

        HttpGet httpGet = new HttpGet(new URI(concatenatePath(this.baseUrl,request.resource)));

        RestResponse restResponse = new RestResponse();
        addHeaderForGet(request,httpGet);

        CloseableHttpResponse httpResponse = null;
        httpResponse = getClient().execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();

        String responseText = "";

        /* process response */
        int statusCode= httpResponse.getStatusLine().getStatusCode();

        /*Construct RestResponse*/
        if (statusCode == 200) {
            responseText = EntityUtils.toString(httpEntity);
            restResponse.setContent(responseText);
        } else {
            throw new Exception("Invalid HTTP response: " + httpResponse.getStatusLine().getStatusCode());
        }

        restResponse.setStatusCode(statusCode);
        restResponse.setHeaders(httpResponse.getAllHeaders());

        return  restResponse;
}

    /**
     * Concatenate string paths
     * @param path
     * @param segment
     * @return
     */
    private String concatenatePath(String path, String segment) {
        if (path == null || path.isEmpty()) {
            return "/" + segment;
        }
        if (segment == null || segment.isEmpty()) {
            return path;
        }

        if (path.charAt(path.length() - 1) == '/') {
            return path + segment;
        }
        return path + "/" + segment;
    }
    /**
     * Returns the Http client instance
     *
     * @return the instance of HttpClient
     */
    public CloseableHttpClient getClient() {
        return client;
    }

    public static void main(String[] args) throws Exception {
//        RestRequest request= new RestRequest();
//        request.addBody("{\"name\": \"anoop\",\"age\": \"a34\"}",DataFormat.JSON);
//        request.headers.put("clientid","hello");
//        request.headers.put("baseid","amazing");
//        request.resource="values";
//        RestClient client=   new RestClient("https://localhost:44353/api");
//        client .invokePost(request);


        RestRequest request= new RestRequest();
        request.resource = "/values";
        request.headers.put("anoopsimon","anoopsimon");
        request.headers.put("version","2.0");

        RestResponse restResponse = new RestClient("https://localhost:44353/api")
                .invokeGet(request);
        for (Header header: restResponse.getHeaders()) {
            System.out.println(header.getName() );
            System.out.println(header.getValue() );
        }

       System.out.println(restResponse.getContent());
        System.out.println(restResponse.getStatusCode());

    }



}