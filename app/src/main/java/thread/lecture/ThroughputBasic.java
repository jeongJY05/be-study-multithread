package thread.lecture;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

public class ThroughputBasic {
    // Latency = T/N (time / threads = cores)
    // maximum performance is between physical core and virtual core.
    // 1st Approach : Breaking task into Subtask
    // 2nd Approach : Running Tasks in Parallel

    private final String INPUT_FILE = "app/resources/war_and_peace.txt";
    private final int NUMBER_OF_THREAD = 2;
    private BookServer server = new BookServer();

    public void execute() {
        try {
            String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
            server.startServer(text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class BookServer {
        private void startServer(String text) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/search", new WordCountHander(text));

            Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREAD);
            server.setExecutor(executor);
            server.start();
        }

        private class WordCountHander implements HttpHandler {
            private String text;

            public WordCountHander(String text) {
                this.text = text;
            }

            @Override
            public void handle(HttpExchange exchange) {
                String query = exchange.getRequestURI().getQuery();
                String [] keyValue = query.split("=");
                String action = keyValue[0];
                String word = keyValue[1];
                if(!action.equals("word")) {
                    try {
                        exchange.sendResponseHeaders(400, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                long count = countWord(word);

                byte [] response = Long.toString(count).getBytes();

                try(OutputStream outputStream = exchange.getResponseBody();) {
                    exchange.sendResponseHeaders(200, response.length);
                    outputStream.write(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private long countWord(String word) {
                long count = 0;
                int index = 0;
                while(index >= 0 ) {
                    index = text.indexOf(word, index);
                    
                    if(index >= 0) {
                        count++;
                        index++;
                    }
                }
                return count;
            }
        
        }
    }
}



