package automation.io.core;

import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.HashMap;

public class RestResponse
{
    private String content;

    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(ProtocolVersion protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    private ProtocolVersion protocolVersion;

    public CloseableHttpResponse getCloseableHttpResponse() {
        return closeableHttpResponse;
    }

    public void setCloseableHttpResponse(CloseableHttpResponse closeableHttpResponse) {
        this.closeableHttpResponse = closeableHttpResponse;
    }

    private CloseableHttpResponse closeableHttpResponse;

    public Header[] getHeaders() {
        return header;
    }

    public void setHeaders(Header[] header) {
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
