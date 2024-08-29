package de.freerider.data_jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import de.freerider.datamodel.DataFactory;
import de.freerider.datamodel.Reservation;

/**
 * Non-public implementation class or DataAccess interface.
 */
@Component
public class DataAccessReservationsImpl  implements DataAccessReservations{

    // Define the desired date-time format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * Datafactory is a component that creates datamodel objects.
     */
    @Autowired
    private DataFactory dataFactory;

    /*
     * JdbcTemplate is the central class in the JDBC core package for SQL
     * database access.
     * - https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html
     * 
     * Examples:
     * - https://mkyong.com/spring-boot/spring-boot-jdbc-examples
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * Run query that returns the number of Reservation in the database:
     * - query: SELECT COUNT(ID) FROM RESERVATION;
     * - returns number extracted from ResultSet.
     * 
     * @return number of Reservation records in the database.
     */
    @Override
    public long countReservations() {
        //
        List<Object> result = jdbcTemplate.query(
            /*
             * Run SQL statement:
             */
            "SELECT COUNT(ID) FROM RESERVATION",

            /*
             * Return ResultSet (rs) and extract COUNT value.
             */
            (rs, rowNum) -> {
                long count = rs.getInt(1);  // index[1]
                return count;
            }
        );
        //
        return result.size() > 0? (long)(result.get(0)) : 0;
    }


    /**
     * Run query that returns all Reservation in the database.
     * - query: SELECT * FROM Reservation;
     * - returns Reservation objects created from ResultSet rows.
     * 
     * @return all Reservation in the database.
     */
    @Override
    public Iterable<Reservation> findAllReservations() {
        //
        var result = jdbcTemplate.queryForStream(
            /*
             * Run SQL statement:
             */
            "SELECT * FROM RESERVATION",

            /*
             * Return ResultSet (rs) and map each row to Optional<Reservation>
             * depending on whether the object could be created from values
             * returned from the database or not (empty Optional is returned).
             */
            (rs, rowNum) -> {
                /*
                 * Extract values from ResultSet for each row.
                 */
                long id = rs.getInt("ID");
                long customer_id = rs.getInt("CUSTOMER_ID");
                long vehicle_id = rs.getInt("VEHICLE_ID");
                String begin = rs.getString("BEGIN");
                String end = rs.getString("END"); 
                String pickup = rs.getString("PICKUP");
                String dropoff = rs.getString("DROPOFF");
                String status = rs.getString("STATUS");

                
                /*
                 * Attempt to create Reservation object through dataFactory,
                 * which returns Optional<Reservation>.
                 */
                return dataFactory.createReservation(id,customer_id, vehicle_id, begin, end, pickup,dropoff, status);
            }
        )
        /*
         * Remove empty results from stream of Optional<Reservation>,
         * map remaining from Optional<Reservation> to Reservation and
         * collect result.
         */
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;    
    }

    /**
     * Run query that returns one Reservations with a given id.
     * - query: SELECT * FROM RESERVATION WHERE ID = ?id;
     * - returns Reservation object created from ResultSet row.
     * 
     * @param id Reservation id (WHERE ID = id)
     * @return Optional with Reservation or empty if not found.
     */
    @Override
    public Optional<Reservation> findReservationById(long id) {
        //
        List<Optional<Reservation>> result = jdbcTemplate.query(
            /*
             * Prepare statement (ps) with "?"-augmented SQL query.
             */
            "SELECT * FROM RESERVATION WHERE ID = ?",
            ps -> {
                /*
                 * Insert id value of first occurence of "?" in SQL.
                 */
                ps.setInt(1, (int)id);
            },

            (rs, rowNum) -> {
                /*
                 * Extract values from ResultSet.
                 */
                long customer_id = rs.getInt("CUSTOMER_ID");
                long vehicle_id = rs.getInt("VEHICLE_ID");
                String begin = rs.getString("BEGIN");
                
                String end = rs.getString("END"); 
                String pickup = rs.getString("PICKUP");
                String dropoff = rs.getString("DROPOFF");
                String status = rs.getString("STATUS");
                /*
                 * Create Optional<Reservation> from values.
                 */
                return dataFactory.createReservation(id,customer_id, vehicle_id, begin, end, pickup,dropoff, status);
            }
        );
        /*
         * Probe List<Optional<Reservation>> and return Optional<Reservation> or
         * empty Optional for empty list.
         */
        return result.size() > 0? result.get(0) : Optional.empty();
    }

    /**
     * Run query that returns all Reservations with matching id in ids.
     * - query: SELECT * FROM RESERVATION WHERE ID IN (145373, 201235, 351682);
     * - returns Reservation objects created from ResultSet rows.
     * 
     * @param ids Reservation ids (WHERE IN (?ids))
     * @return Reservation with matching ids.
     */
    @Override
    public Iterable<Reservation> findAllReservationsById(Iterable<Long> ids) {
        /*
         * Map ids (145373, 201235, 351682) to idsStr: "145373, 201235, 351682"
         */
        String idsStr = StreamSupport.stream(ids.spliterator(), false)
            .map(id -> String.valueOf(id))
            .collect(Collectors.joining(", "));
        //
        var result = jdbcTemplate.queryForStream(
            /*
             * Prepare statement (ps) with "?"-augmented SQL query.
             */
            String.format("SELECT * FROM RESERVATION WHERE ID IN (%s)", idsStr),

            /*
             * Extract values from ResultSet for each row.
             */
            (rs, rowNum) -> {
                long id = rs.getInt("ID");
                long customer_id = rs.getInt("CUSTOMER_ID");
                long vehicle_id = rs.getInt("VEHICLE_ID");
                String begin = rs.getString("BEGIN");
                String end = rs.getString("END"); 
                String pickup = rs.getString("PICKUP");
                String dropoff = rs.getString("DROPOFF");
                String status = rs.getString("STATUS");
                /*
                 * Create Optional<Reservation> from values.
                 */
                return dataFactory.createReservation(id,customer_id, vehicle_id, begin, end, pickup,dropoff, status);
            }
        )
        /*
         * Remove empty results from stream of Optional<Customer>,
         * map remaining from Optional<Customer> to Customer and
         * collect result.
         */
        .filter(opt -> opt.isPresent())
        .map(opt -> opt.get())
        .collect(Collectors.toList());
        //
        return result;
    }

    @Override
    public Reservation createReservation(Map<String, Object> map) throws DataAccessException {
        final Object[] attrs = {null, null, null, null, null, null, null, null};
        int id = -1;

        for (String key : map.keySet()) {
            switch (key.toUpperCase()) {
                case "ID":
                    final Integer ID = parseNumber(map.get(key));
                    id = ID != null ? ID : -1;
                    attrs[0] = id >= 0 ? ID : null;
                    break;
                case "CUSTOMER_ID":
                    attrs[1] = parseNumber(map.get(key));
                    break;
                case "VEHICLE_ID":
                    attrs[2] = parseNumber(map.get(key));
                    break;
                case "BEGIN":
                    attrs[3] = map.get(key);
                    break;
                case "END":
                    attrs[4] = map.get(key);
                    break;
                case "PICKUP":
                    attrs[5] = map.get(key);
                    break;
                case "DROPOFF":
                    attrs[6] = map.get(key);
                    break;
                case "STATUS":
                    attrs[7] = map.get(key);
                    break;
            }
        }

        for (Object a : attrs) {
            if (a == null) {
                throw new DataAccessException.BadRequest("incomplete attributes");
            }
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            int created = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO RESERVATION (ID, CUSTOMER_ID, VEHICLE_ID, BEGIN, END, PICKUP, DROPOFF, STATUS) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                        Statement.RETURN_GENERATED_KEYS);

                ps.setLong(1, (Long) attrs[0]);  // ID
                ps.setLong(2, (Long) attrs[1]);  // CUSTOMER_ID
                ps.setLong(3, (Long) attrs[2]);  // VEHICLE_ID
                ps.setString(4, (String) attrs[3]);  // BEGIN
                ps.setString(5, (String) attrs[4]);  // END
                ps.setString(6, (String) attrs[5]);  // PICKUP
                ps.setString(7, (String) attrs[6]);  // DROPOFF
                ps.setString(8, (String) attrs[7]);  // STATUS

                return ps;
            }, keyHolder);

            if (created != 1) {
                throw new DataAccessException.BadRequest(
                        String.format("data record not created for id: %d, %d records created", id, created)
                );
            }
        } catch (org.springframework.dao.DataAccessException dax) {
            throw new DataAccessException.Conflict("INSERT exception, id may exist: " + (int)attrs[0]);
        }

        return dataFactory.createReservation(
                (Long) attrs[0],
                (Long) attrs[1],
                (Long) attrs[2],
                (String) attrs[3],
                (String) attrs[4],
                (String) attrs[5],
                (String) attrs[6],
                (String) attrs[7]
        ).orElseThrow(() ->
                new DataAccessException.Conflict("failed to create object for id: " + (int)attrs[0]));
    }

    @Override
    public boolean updateReservation(Map<String, Object> map) throws DataAccessException {
        StringBuilder cols = new StringBuilder();
        int id = -1;

        // Extract attributes from map
        for (String key : map.keySet()) {
            switch (key.toUpperCase()) {
                case "ID":
                    final Integer ID = parseNumber(map.get(key));
                    id = (ID != null && ID >= 0) ? ID : id;
                    if (id >= 0) {
                        cols.append(cols.length() > 0 ? ", " : "").append("ID=").append(map.get(key));
                    }
                    break;
                case "CUSTOMER_ID":
                    cols.append(cols.length() > 0 ? ", " : "").append("CUSTOMER_ID=").append(map.get(key));
                    break;
                case "VEHICLE_ID":
                    cols.append(cols.length() > 0 ? ", " : "").append("VEHICLE_ID=").append(map.get(key));
                    break;
                case "BEGIN":
                    cols.append(cols.length() > 0 ? ", " : "").append("BEGIN='").append(map.get(key)).append("'");
                    break;
                case "END":
                    cols.append(cols.length() > 0 ? ", " : "").append("END='").append(map.get(key)).append("'");
                    break;
                case "PICKUP":
                    cols.append(cols.length() > 0 ? ", " : "").append("PICKUP='").append(map.get(key)).append("'");
                    break;
                case "DROPOFF":
                    cols.append(cols.length() > 0 ? ", " : "").append("DROPOFF='").append(map.get(key)).append("'");
                    break;
                case "STATUS":
                    cols.append(cols.length() > 0 ? ", " : "").append("STATUS='").append(map.get(key)).append("'");
                    break;
            }
        }

        // Probe all values have been set
        if (cols.length() > 0 && id >= 0) {
            try {
                final String fcols = cols.toString();
                final long fid = id;
                int updated = jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(
                            "UPDATE RESERVATION SET " + fcols + " WHERE ID = ?;");
                    ps.setLong(1, fid);
                    return ps;
                });

                if (updated != 1) {
                    throw new DataAccessException.NotFound(
                            String.format("id not found: %d, %d records updated", id, updated)
                    );
                }
            } catch (org.springframework.dao.DataAccessException dax) {
                throw new DataAccessException.BadRequest(dax.getMessage());
            }
        } else {
            throw new DataAccessException.BadRequest("incomplete attributes");
        }
        return true;
    }

    @Override
    public boolean deleteReservation(long id) throws DataAccessException {
        //
        if(id < 0)
            throw new DataAccessException.BadRequest("invalid id: " + id);
        //
        try {
            //
            int deleted = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                    .prepareStatement("DELETE FROM RESERVATION WHERE ID = ?;");
                ps.setInt(1, (int)id);
                return ps;
            });
            //
            if(deleted != 1) {
                throw new DataAccessException.NotFound(
                    String.format("id not found: %d, %d records deleted", id, deleted)
                );
            }
        //
        } catch(org.springframework.dao.DataAccessException dax) {
            throw new DataAccessException.Conflict("conflict deleting item id: " +
                        id + ", foreign key dependency may exist");
        }
        return true;
    }

    /**
     * Attempt to parse an Integer value from an object.
     * 
     * @param obj object to parse.
     * @return Integer value or null.
     */
    private Integer parseNumber(Object obj) {
        Integer number = null;
        if(obj != null) {
            if(obj instanceof Integer)
            number = (Integer)obj;
            else if(obj instanceof String)
                try {
                    number = Integer.parseInt((String)obj);
                } catch(NumberFormatException ex) { };
            }
        return number;
    }
    
}
