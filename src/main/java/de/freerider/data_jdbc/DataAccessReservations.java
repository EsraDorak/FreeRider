package de.freerider.data_jdbc;

import java.util.Map;
import java.util.Optional;

import de.freerider.datamodel.Reservation;


/**
 * Public data Reservation DataAccess interface.
 * 
 * @author majdu94
 */
public interface DataAccessReservations {

    /**
     * Run query that returns the number of Reservations in the database:
     * - query: SELECT COUNT(ID) FROM Reservation;
     * - returns number extracted from ResultSet.
     * 
     * @return number of Reservation records in the database.
     */
    long countReservations();


    /**
     * Run query that returns all Reservations in the database.
     * - query: SELECT * FROM Reservation;
     * - returns Reservation objects created from ResultSet rows.
     * 
     * @return all Reservations in the database.
     */
    Iterable<Reservation> findAllReservations();


    /**
     * Run query that returns one Reservations with a given id.
     * - query: SELECT * FROM Reservation WHERE ID = 145373;
     * - returns Reservation object created from ResultSet row.
     * 
     * @param id Reservation id (WHERE ID = ?id)
     * @return Optional with Reservation or empty if not found.
     */
    Optional<Reservation> findReservationById(long id);


    /**
     * Run query that returns all Reservations with matching id in ids.
     * - query: SELECT * FROM Reservation WHERE ID IN (10, 20, 30000, 40);
     * - returns Reservation objects created from ResultSet rows.
     * 
     * @param ids Reservation ids (WHERE IN (?ids))
     * @return Reservations with matching ids.
     */
    Iterable<Reservation> findAllReservationsById(Iterable<Long> ids);


    /**
     * Attempt to INSERT new record into Reservation table from attributes
     * provided by name-value pairs, e.g.:
     * <pre>
     * [
     *   "id": 48733,
     *   "CUSTOMER_ID": 54,
     *   "VEHICLE_ID": 1002,
     *   "BEGIN": "20/12/2022 10:00:00"
     *   "END" : "20/12/2022 20:00:00"
     *   "PICKUP", "Berlin Wedding"
     *   "DROPOFF", "Berlin Wedding"
     *   "STATUS" : "Booked"
     * ]
     * </pre>
     * If Reservation data could be inserted into the database, a Reservation
     * object is returned. An exception is thrown otherwise with error
     * code: 400 bad request (invalid attributes), 409 conflict (id exists).
     * 
     * @param map name-value pairs of Reservation data.
     * @return created Reservation object.
     * @throws DataAccessException with error code (400 bad request, 409 conflict).
     */
    Reservation createReservation(Map<String, Object> map) throws DataAccessException;


    /**
     * Attempt to UPDATE existing record into Reservation table from attributes
     * provided by name-value pairs, e.g.:
     * <pre>
     * [
     *   "id": 48733,                               <-- must be present
     *   "BEGIN": "20/12/2022 11:00:00",    <-- updated data element
     * ]
     * </pre>
     * The update Reservation object is returned with success. An exception is
     * thrown otherwise with error code: 400 bad request (invalid attributes),
     * 404 not found (id not found).
     * 
     * @param map name-value pairs of Reservation data.
     * @return true if Reservation was updated sucessfully.
     * @throws DataAccessException with error code (400 bad request, 404 not found).
     */
    boolean updateReservation(Map<String, Object> map) throws DataAccessException;


    /**
     * Delete Reservation record with id from Reservation table. An exception is
     * thrown with error code: 404 not found (id not found), 409 conflict
     * (foreign key violation).
     * 
     * @param id Reservation id.
     * @return true if Reservation was deleted sucessfully.
     * @throws DataAccessException with error code (404 not found, 409 conflict).
     */
    boolean deleteReservation(long id) throws DataAccessException;

}
