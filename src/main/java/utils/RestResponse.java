package utils;

import org.apache.http.Header;

import java.util.HashMap;

public class RestResponse
{
    private String content;

    public Header[] getHeaders() {
        return header;
    }

    public void setHeader(Header[] header) {
        this.header = header;
    }

    private Header[] header;
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;
    private HashMap<String,String> headers;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }


}
