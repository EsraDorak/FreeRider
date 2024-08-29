package de.freerider.endpoints;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import de.freerider.datamodel.Vehicle;


/**
 * Spring Controller interface for /vehicles REST endpoint to access the
 * collection of Vehicle resources maintained in a VehicleRepository.
 * 
 * Operations provided by the endpoint:
 * 
 * - GET /vehicles         - return JSON data for all Vehicle in the repository,
 *                            status: 200 OK.
 * 
 * - GET /vehicles/{id}    - return JSON data for Vehicle with id,
 *                            status: 200 OK, 400 bad request (id), 404 not found.
 * 
 * 
 * @author sgra64
 *
 */

@RequestMapping("/v1/vehicles")
public interface VehiclesEP extends VehiclesEPDoc {

    @GetMapping("")
    @Override
    Iterable<Vehicle> findAllVehicles();


    @GetMapping("/{id}")
    @Override
    Vehicle findVehicleById(@PathVariable long id);

}
