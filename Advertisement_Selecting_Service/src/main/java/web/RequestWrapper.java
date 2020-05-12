package web;

import org.apache.commons.io.IOUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class RequestWrapper extends HttpServletRequestWrapper {
    private final String content;

    public String getContent() {
        return this.content;
    }

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        content = IOUtils.toString(request.getReader());
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes());
        return new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }
}
