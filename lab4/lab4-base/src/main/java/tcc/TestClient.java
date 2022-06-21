package tcc;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tcc.flight.FlightReservationDoc;
import tcc.hotel.HotelReservationDoc;

/**
 * Simple non-transactional client. Can be used to populate the booking services
 * with some requests.
 */
public class TestClient {
    public static void main(String[] args) {
        // for(int i = 0; i < 10; i++) // Results in HTTP 409 -> Conflict, Server Exception MAX_NUM_OF_BOOKINGS
        newMain();

        // ACID
        // Atomicity: Client books either hotel and flight or nothing at all
        // Consistency: a consistent state is reached once the confirmation was successful
        // Isolation: transactions aren't really isolated, because they see unconfirmed reservations from other transactions, but by ignoring unconfirmed reservations, isolation can be maintained
        // Durability: has to be guaranteed by the server, in this case all data is stored in volatile memory

    }

    private static void oldMain() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(TestServer.BASE_URI);

            GregorianCalendar tomorrow = new GregorianCalendar();
            tomorrow.setTime(new Date());
            tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);

            // book flight

            WebTarget webTargetFlight = target.path("flight");

            FlightReservationDoc docFlight = new FlightReservationDoc();
            docFlight.setName("Christian");
            docFlight.setFrom("Karlsruhe");
            docFlight.setTo("Berlin");
            docFlight.setAirline("airberlin");
            docFlight.setDate(tomorrow.getTimeInMillis());

            Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docFlight));

            if (responseFlight.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseFlight.getStatus());
            }

            FlightReservationDoc outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
            System.out.println("Output from Server: " + outputFlight);

            // book hotel

            WebTarget webTargetHotel = target.path("hotel");

            HotelReservationDoc docHotel = new HotelReservationDoc();
            docHotel.setName("Christian");
            docHotel.setHotel("Interconti");
            docHotel.setDate(tomorrow.getTimeInMillis());

            Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docHotel));

            if (responseHotel.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseHotel.getStatus());
            }

            HotelReservationDoc outputHotel = responseHotel.readEntity(HotelReservationDoc.class);
            System.out.println("Output from Server: " + outputHotel);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void newMain() {
        try {
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(TestServer.BASE_URI);

            GregorianCalendar tomorrow = new GregorianCalendar();
            tomorrow.setTime(new Date());
            tomorrow.add(GregorianCalendar.DAY_OF_YEAR, 1);

            // book flight

            WebTarget webTargetFlight = target.path("flight");

            FlightReservationDoc docFlight = new FlightReservationDoc();
            docFlight.setName("Christian");
            docFlight.setFrom("Karlsruhe");
            docFlight.setTo("Berlin");
            docFlight.setAirline("airberlin");
            docFlight.setDate(tomorrow.getTimeInMillis());

            Response responseFlight = webTargetFlight.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docFlight));

            if (responseFlight.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseFlight.getStatus());
            }

            FlightReservationDoc outputFlight = responseFlight.readEntity(FlightReservationDoc.class);
            System.out.println("Output from Server: " + outputFlight);

            // book hotel

            WebTarget webTargetHotel = target.path("hotel");

            HotelReservationDoc docHotel = new HotelReservationDoc();
            docHotel.setName("Christian");
            docHotel.setHotel("Interconti");
            docHotel.setDate(tomorrow.getTimeInMillis());

            Response responseHotel = webTargetHotel.request().accept(MediaType.APPLICATION_XML)
                    .post(Entity.xml(docHotel));

            if (responseHotel.getStatus() != 200) {
                System.out.println("Failed : HTTP error code : " + responseHotel.getStatus());
            }

            HotelReservationDoc outputHotel = responseHotel.readEntity(HotelReservationDoc.class);
            System.out.println("Output from Server: " + outputHotel);


            if (responseHotel.getStatus() != 200 || responseFlight.getStatus() != 200) {
                // cancel flight booking
                try {
                    WebTarget cancelFlightTarget = client.target(outputFlight.getUrl());
                    Response deleteFlightResponse = cancelFlightTarget.request().accept(MediaType.TEXT_PLAIN).delete();
                    System.out.println("Cancel flight response: " + deleteFlightResponse);
                } catch (Exception e) {
                    System.out.println("Cancel flight request failed" + e.getMessage());
                }

                // cancel hotel booking
                try {
                    WebTarget deleteHotelTarget = client.target(outputHotel.getUrl());
                    Response deleteHotelResponse = deleteHotelTarget.request().accept(MediaType.TEXT_PLAIN).delete();
                    System.out.println("Cancel hotel response: " + deleteHotelResponse);
                } catch (Exception e) {
                    System.out.println("Cancel hotel request failed" + e.getMessage());
                }

                System.out.println("Canceled preliminary bookings");
                return;
            }

            // confirm flight booking
            WebTarget confirmFlightTarget = client.target(outputFlight.getUrl());
            Response confirmFlightResponse = confirmFlightTarget.request().accept(MediaType.TEXT_PLAIN).put(Entity.json(""));
            System.out.println("Confirm flight response: " + confirmFlightResponse);

            // cancel everything if flight confirmation fails
            if (confirmFlightResponse.getStatus() != 200) {
                try {
                    WebTarget cancelFlightTarget = client.target(outputFlight.getUrl());
                    Response deleteFlightResponse = cancelFlightTarget.request().accept(MediaType.TEXT_PLAIN).delete();
                    System.out.println("Cancel flight response: " + deleteFlightResponse);
                } catch (Exception e) {
                    System.out.println("Cancel flight request failed" + e.getMessage());
                }

                // cancel hotel booking
                try {
                    WebTarget deleteHotelTarget = client.target(outputHotel.getUrl());
                    Response deleteHotelResponse = deleteHotelTarget.request().accept(MediaType.TEXT_PLAIN).delete();
                    System.out.println("Cancel hotel response: " + deleteHotelResponse);
                } catch (Exception e) {
                    System.out.println("Cancel hotel request failed" + e.getMessage());
                }

                System.out.println("Canceled preliminary bookings");
                return;
            }

            // confirm hotel booking - multiple tries to make sure
            WebTarget confirmHotelTarget = client.target(outputHotel.getUrl());
            Response confirmHotelResponse;
            int requests = 0;
            final int MAX_REPEAT_REQUESTS = 100;
            do {
                confirmHotelResponse = confirmHotelTarget.request().accept(MediaType.TEXT_PLAIN).put(Entity.json(""));
                requests++;
            } while (confirmHotelResponse.getStatus() != 200 && requests < MAX_REPEAT_REQUESTS);

            System.out.println("Confirm flight response: " + confirmHotelResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
