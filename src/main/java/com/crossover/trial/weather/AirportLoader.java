package com.crossover.trial.weather;

import com.crossover.trial.weather.helper.CsvHelper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * A simple airport loader which reads a file from disk and sends entries to the webservice
 * <p>
 * TODO: Implement the Airport Loader
 *
 * @author code test administrator
 */
public class AirportLoader {

    /**
     * Helper class for csv operation.
     */
    private final CsvHelper csvHelper = new CsvHelper();

    /**
     * End point for read queries.
     */
    private WebTarget query;

    /**
     * End point to supply updates.
     */
    private WebTarget collect;

    public AirportLoader(String baseUrl) {
        Client client = ClientBuilder.newClient();
        collect = client.target(baseUrl + "/collect");
    }

    public static void main(String args[]) throws IOException {
        //File airportDataFile = new File(args[0]);
        File airportDataFile = new File("src/main/resources/airports.dat");
        if (!airportDataFile.exists() || airportDataFile.length() == 0) {
            System.err.println(airportDataFile + " is not a valid input");
            System.exit(1);
        }

        AirportLoader al = new AirportLoader("http://localhost:9090");
        al.upload(new FileInputStream(airportDataFile));
        System.exit(0);
    }

    public void upload(InputStream airportDataStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(airportDataStream, "UTF-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                extractAirportItemAndCollect(line);

            }
        }

    }

    private void extractAirportItemAndCollect(String readedLine) {
        String[] columnsOfReadedLine = csvHelper.split(readedLine);
        if (columnsOfReadedLine == null || columnsOfReadedLine.length <= 7) {
            return;
        }
        String path = getRequestPath(columnsOfReadedLine);
        Response post = collect.path(path).request().post(Entity.text(""));
        String iataCode = extractAirportIataCodeFromReadedLine(columnsOfReadedLine);
        processResultStatus(iataCode, post);
    }

    private String extractAirportIataCodeFromReadedLine(String[] readedLine) {
        return readedLine[4].replace("\"", "");
    }

    private String getRequestPath(String[] columnsOfReadedLine) {
        String iataCode = extractAirportIataCodeFromReadedLine(columnsOfReadedLine);
        String latitude = columnsOfReadedLine[6];
        String longitude = columnsOfReadedLine[7];
        return "/airport/" + iataCode + "/" + latitude + "/" + longitude;
    }

    private void processResultStatus(String iataCode, Response post) {
        switch (post.getStatus()) {
            case 200:
                break;

            case 403:
                System.out.println("Warning: airport entry '" + iataCode + "' already exists");
                break;

            default:
                System.out.println("ERROR when adding airport '" + iataCode + "': " + post.getStatus() + " " + post.getStatusInfo());
        }
    }

}
