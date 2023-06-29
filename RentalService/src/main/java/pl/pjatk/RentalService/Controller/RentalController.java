package pl.pjatk.RentalService.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.RentalService.Movie.Movie;
import pl.pjatk.RentalService.Service.RentalService;

@RestController
@RequestMapping("/rental")
public class RentalController {

    private final RentalService rentalService;


    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @Operation(summary = "Get info of movie by chosen Id from DB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found.",
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        if (rentalService.getMovieById(id) == null) {
            rentalService.checkError(id);
        }
        return ResponseEntity.ok(rentalService.getMovieById(id));
    }

    @Operation(summary = "Set movies availability to 'true' by chosen Id from DB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie available.",
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @PutMapping("setAv/{id}")
    public ResponseEntity<Movie> returnMovie(@PathVariable Long id) {
        rentalService.makeAvailable(id);
        rentalService.checkError(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Set movies availability to 'false' by chosen Id from DB.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie unavailable.",
                    content = @Content(schema = @Schema(implementation = Movie.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @PutMapping("setNAv/{id}")
    public ResponseEntity<Movie> rentMovie(@PathVariable Long id) {
        rentalService.makeNotAvailable(id);
        return ResponseEntity.ok().build();
    }

}
