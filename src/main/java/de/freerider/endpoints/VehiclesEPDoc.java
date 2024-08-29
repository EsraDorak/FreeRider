package de.freerider.endpoints;

import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;

import de.freerider.datamodel.Vehicle;


public interface VehiclesEPDoc {

    static final String api_group_vehicles = "Vehicles endpoint";


    /**
     * Return all vehicle.
     * 
     * - GET /vehicles
     * 
     * @return iterable with all Vehicles.
     */
    @Operation(
        // group name where this operation appears and defines swagger tag
        // http://localhost:8080/swagger-ui/index.html#/vehicles-controller
        tags = {api_group_vehicles},

        // summary: single-line description in API short-list
        summary = "Return all vehicles.",

        // detailed description inside API
        description = "Return all vehicles (no limit, no pagination)."
    )
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="OK", content=@Content(mediaType="application/json")),
    })
    //
    Iterable<Vehicle> findAllVehicles();


    /**
     * Return Vehicle by id.
     * 
     * - GET /vehicles/{id}
     * 
     * @param id of Vehicle, id must not be negative.
     * @return Vehicle with id.
     * @throws ResponseStatusException 400 bad request, 404 not found.
     */
    @Operation(
        tags = {api_group_vehicles},
        summary = "Return Vehicle by id, id must not be negative.",
        description = "Return Vehicle if id exists, else return error 404 (not found)."
        // parameters = {@Parameter(name="id", in=ParameterIn.QUERY, schema=@Schema(implementation=Long.class)) }
    )
    @ApiResponses(value={
        @ApiResponse(responseCode="200", description="OK", content=@Content(mediaType="application/json")),
        @ApiResponse(responseCode="400", description="Bad Request"),
        @ApiResponse(responseCode="404", description="Not Found"),
    })
    //
    Vehicle findVehicleById(@PathVariable long id);

}
