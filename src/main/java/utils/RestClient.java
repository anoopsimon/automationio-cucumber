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

import java.io.IOException;
import java.net.URI;
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

    public RestClient()
    {
        this.client = initializeHttpClient();
        this.baseUrl="";
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
     * To Generate the Rest Response object
     * @param response
     * @return
     * @throws IOException
     */
    private RestResponse generateRestResponse(CloseableHttpResponse response) throws IOException {
        String content = EntityUtils.toString(response.getEntity());
        RestResponse restResponse = new RestResponse();
        restResponse.setContent(content);
        restResponse.setStatusCode(response.getStatusLine().getStatusCode());
        restResponse.setHeaders(response.getAllHeaders());
        restResponse.setCloseableHttpResponse(response);
        restResponse.setProtocolVersion(response.getStatusLine().getProtocolVersion());
        return restResponse;


    }

    /**
     * Invoke rest API endpoint having PUT method
     * @param request
     * @return
     * @throws Exception
     */
    public RestResponse put(RestRequest request) throws Exception {
        RestResponse restResponse = new RestResponse();
        HttpPut httpPut = new HttpPut(new URI(concatenatePath(this.baseUrl,request.resource)));
        addHeaderForPut(request,httpPut);
        StringEntity entity = new StringEntity(request.getBody());
        httpPut.setEntity(entity);
        CloseableHttpResponse response = getClient().execute(httpPut);
        checkResponseCode(response);

        /*Construct RestResponse*/
        restResponse =  generateRestResponse(response);
        this.getClient().close();

        return restResponse;
    }



    /**
     * To Invoke a Rest API endpoint having POST Method
     * @param request
     * @return
     * @throws Exception
     */
    public RestResponse post(RestRequest request) throws Exception {

        /*initialize POST client*/
        HttpPost httpPost = new HttpPost(new URI(concatenatePath(this.baseUrl,request.resource)));
        addContentType(httpPost,request.dataFormat);

        StringEntity entity = new StringEntity(request.getBody());
        httpPost.setEntity(entity);
        addHeaderForPost(request,httpPost);

        /*Execute post*/
        CloseableHttpResponse response = getClient().execute(httpPost);
        checkResponseCode(response);

        /*Construct RestResponse*/
        RestResponse restResponse  =  generateRestResponse(response);

        this.getClient().close();
        return restResponse;
    }

    public RestResponse get(String resource) throws Exception {
        RestRequest request = new RestRequest();
        request.setResource(resource);
        return get(request);

    }


    /**
     * To Invoke a Rest API endpoint having GET Method
     * @param request
     * @return RestResponse
     * @throws Exception
     */
    public RestResponse get(RestRequest request) throws Exception {

        HttpGet httpGet = new HttpGet(new URI(concatenatePath(this.baseUrl,request.resource)));
        addHeaderForGet(request,httpGet);

        CloseableHttpResponse httpResponse = null;
        try{
            httpResponse = getClient().execute(httpGet);
        }catch (IllegalStateException e){
            throw new IllegalAccessException("Create a new RestClient instance to proceed with next api call. Client is disposed after every api call");
        }

        checkResponseCode(httpResponse);

        /*Construct RestResponse*/
        RestResponse restResponse  =  generateRestResponse(httpResponse);
        this.getClient().close();
        return  restResponse;
}

    private void checkResponseCode(CloseableHttpResponse response) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new Exception("Invalid HTTP response: " + statusCode);
        }
    }


    /**
     * Concatenate string paths
     * @param path
     * @param segment
     * @return
     */
    private String concatenatePath(String path, String segment) {
        if (path == null || path.isEmpty()) {
            return "" + segment;
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
        RestResponse r =  new RestClient().get("https://localhost:44353/api/values");
        System.out.println(r.getContent());

        RestRequest request= new RestRequest();
        request
                //.addHeader("","")
                // .addHeader("","")
                 .addBody("{\"name\": \"anoop\",\"age\": \"a34\"}",DataFormat.JSON)
                 .setResource("values");

        RestClient client=   new RestClient("https://localhost:44353/api");
        client .post(request);

        RestRequest request1= new RestRequest();
        request1.setResource("/values")
                .addHeader("anoopsimon","anoopsimon")
                .addHeader("version","v1");


        //

        RestResponse restResponse = new RestClient("https://localhost:44353/api")
                .get(request1);
        for (Header header: restResponse.getHeaders()) {
            System.out.println(header.getName() );
            System.out.println(header.getValue() );
        }

       System.out.println(restResponse.getContent());
        System.out.println(restResponse.getStatusCode());




    }





}
