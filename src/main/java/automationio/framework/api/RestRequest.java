package automationio.framework.api;

import java.util.HashMap;

public class RestRequest
{
    public String resource;
    public DataFormat dataFormat;
    public String getBody() {
        return body;
    }

    public RestRequest addBody(String body,DataFormat dataFormat) {
        this.body = body;
        this.dataFormat=dataFormat;

        return this;
    }

    private String body;


    public RestRequest(){
        this.headers = new HashMap<String, String>();
    }

    public String getResource() {
        return resource;
    }

    public RestRequest setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public RestRequest addHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RestRequest addHeader(String header , String value) {
        if(header==null || header.isEmpty()) return this;
        this.headers.put(header,value);
        return this;
    }

    HashMap<String,String> headers;

}
