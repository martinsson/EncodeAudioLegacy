package encode.audio.entrypoint;

import static com.github.dreamhead.moco.Moco.*;
import static com.github.dreamhead.moco.Runner.runner;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runner;

public class MocoTest {

    private Runner runner;
    HttpServer server = httpserver(12306);

    @Before
    public void setuphttp() {
        runner = runner(server);
        runner.start();
    }

    @After
    public void tearDown() {
        runner.stop();
    }
    
    @Test
    public void should_response_as_expected() throws IOException {
//        server.response("bar");
        server.response(with(file("./src/test/resources/10.151.156.180Mon_Nov_04_140724_CET_2013343.wav")));

        Content content = Request.Get("http://localhost:12306").execute().returnContent();
        assertThat(content.asBytes()).isNotEmpty();
    }
}
