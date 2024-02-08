import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class BitCoinTracker {
    public static void main(String... args) {
        List<String> bitcoin = new ArrayList<>();

        while(true) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    BufferedWriter writer = null;
                    try {
                        Thread.sleep(200);
                        System.out.println("Shutting down ...");
                        File bcoinFile = new File("C:\\Users\\bta96367\\IdeaProjects\\rest_test_project\\src\\main\\java\\output.csv");
                        writer = new BufferedWriter(new FileWriter(bcoinFile));
                        for (String bit : bitcoin) {
                            writer.write(bit + "\n");
                        }
                    } catch (InterruptedException | IOException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    } finally {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder(
                            URI.create("https://api.coindesk.com/v1/bpi/currentprice.json"))
                    .build();
            HttpResponse<String> response = null;
            while (true) {
                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    Bitcoin bcoin = new Gson().fromJson(response.body(), Bitcoin.class);
                    String time = bcoin.getTime().getUpdated();
                    String bitcoinValue = bcoin.getBpi().getUSD().getRate();
                    System.out.println(time + " = $" + bitcoinValue);
                    bitcoin.add(time + ", $" + bitcoinValue);
                    Thread.sleep(60000);
                } catch (IOException e) {
                    System.out.println("ERROR");
                } catch (InterruptedException e) {
                    System.out.println("error");
                }
            }
        }
    }
}

