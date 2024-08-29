package de.freerider.endpoints;

//import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import de.freerider.data_jdbc.DataAccess;
//import de.freerider.data_jdbc.DataAccessException;
import de.freerider.data_jdbc.DataAccessVehicles;
import de.freerider.datamodel.Vehicle;


@RestController
class VehiclesRestController implements VehiclesEP {

    /*
     * Logger instance for this class.
     */
    private static final Logger logger =
        LoggerFactory.getLogger(VehiclesRestController.class);

    /**
     * DataAccess (object) vehicle_dao is a component to accesses data in the
     * database through SQL queries.
     */
    @Autowired
    private DataAccessVehicles vehicle_dao;


    @Override
    public Iterable<Vehicle> findAllVehicles() {
        return vehicle_dao.findAllVehicles();
    }


    @Override
    public Vehicle findVehicleById(@PathVariable long id) {
        //
        logger.info(String.format("--- received request: GET /Vehicle/%d", id));
        //
        if(id < 0L)
            // throw error 400 (bad request)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("Vehicle id: %d negative", id, HttpStatus.BAD_REQUEST.value())
            );
        //
        Vehicle found = vehicle_dao.findVehicleById(id)
            .map(c -> c)    // return Vehicle{id}, if found
            //
            //              // else throw error 404 (not found)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Vehicle id: %d not found, error %d", id, HttpStatus.NOT_FOUND.value())
            ));
        //
        logger.info(String.format("--- found: Vehicle(id: %d, name: %s)", found.getId(), found.getMake()));
        //
        return found;
    }

/* 
    @Override
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Map<String, Object> jsonData) {
        //
        logger.info(String.format("--- received POST (create): Vehicle JSON data:"));
        //
        try {
            //
            Vehicle Vehicle = vehicle_dao.createVehicle(jsonData);
            //
            logger.info(String.format("--- new Vehicle object created: [%d, \"%s\", \"%s\", \"%s\"]",
                Vehicle.getId(), Vehicle.getName(), Vehicle.getContact(), Vehicle.getStatus()
            ));
            //
            // return Vehicle object (serialized to JSON)
            return ResponseEntity.status(HttpStatus.CREATED).body(Vehicle);
        //
        } catch(DataAccessException dax) {
            reThrow(dax, "DataAccessException dax: " + dax.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }


    @Override
    public ResponseEntity<?> updateVehicle(Map<String, Object> jsonData) {
        //
        logger.info(String.format("--- received PUT (update): Vehicle JSON data:"));
        //
        var respCode = HttpStatus.NOT_IMPLEMENTED;
        try {
            vehicle_dao.updateVehicle(jsonData);
            logger.info(String.format("--- Vehicle object updated"));
            respCode = HttpStatus.ACCEPTED;
        //
        } catch(DataAccessException dax) {
            reThrow(dax, "DataAccessException dax: " + dax.getMessage());
        }
        return ResponseEntity.status(respCode).build();
    }


    @Override
    public ResponseEntity<?> deleteVehicleById(long id) {
        //
        logger.info(String.format("--- received request: DELETE /Vehicle/%d", id));
        //
        var respCode = HttpStatus.NOT_IMPLEMENTED;
        try {
            vehicle_dao.deleteVehicle(id);
            respCode = HttpStatus.ACCEPTED;
        //
        } catch(DataAccessException dax) {
            reThrow(dax, "DataAccessException dax: " + dax.getMessage());
        }
        return ResponseEntity.status(respCode).build();
    }
*/

    /**
     * Map exceptions of type DataAccessException used in the data access layer
     * to HTTP ResponseStatusExceptions used in the Controller layer.
     * 
     * @param dax DataAccessException from the data access layer.
     * @param msg exception message.
     * @throws ResponseStatusException return to HTTP client.
     */
    /* 
    private void reThrow(DataAccessException dax, String msg) throws ResponseStatusException {
        var respCode = HttpStatus.NOT_IMPLEMENTED;
        switch(dax.code) {
            case BadRequest: respCode = HttpStatus.BAD_REQUEST; break;
            case NotFound:   respCode = HttpStatus.NOT_FOUND; break;
            case Conflict:   respCode = HttpStatus.CONFLICT; break;
        }
        throw new ResponseStatusException(respCode, msg);
    }
    */
}
