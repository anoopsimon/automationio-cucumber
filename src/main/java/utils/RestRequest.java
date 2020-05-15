package utils;

import java.util.HashMap;

public class RestRequest
{
    public String resource;
    public DataFormat dataFormat;
    public String getBody() {
        return body;
    }

    public void addBody(String body,DataFormat dataFormat) {
        this.body = body;
        this.dataFormat=dataFormat;
    }

    private String body;


    public RestRequest(){
        this.headers = new HashMap<String, String>();
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    HashMap<String,String> headers;

}
