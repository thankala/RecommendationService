package com.xm.recommendationservice.api.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.xm.recommendationservice.api.responses.ApiErrorResponse;
import com.xm.recommendationservice.domain.dtos.CryptoNormalizedRange;
import com.xm.recommendationservice.domain.dtos.CryptoStats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Cryptocurrencies", description = "Crypto stats and range analysis")
public interface CryptoApi {

  @Operation(summary = "Get stats (oldest, newest, min, max) for a crypto", description = "Optionally provide a date range to filter the results.", responses = {
      @ApiResponse(responseCode = "200", description = "Stats successfully retrieved", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CryptoStats.class), examples = @ExampleObject(name = "ETH Stats", summary = "Example response for Ethereum", value = """
          {
            "symbol": "ETH",
            "oldest": 3715.32,
            "newest": 2672.5,
            "min": 2336.52,
            "max": 3828.11,
            "oldestTimestamp": "2022-01-01T08:00:00Z",
            "newestTimestamp": "2022-01-31T20:00:00Z"
          }
          """))),
      @ApiResponse(responseCode = "404", description = "Crypto not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Crypto Not Found", value = """
          {
            "timestamp": "2025-04-18T18:21:03.123Z",
            "status": 404,
            "error": "Not Found",
            "message": "Crypto symbol 'ABC' not found"
          }
          """)))
  })
  ResponseEntity<CryptoStats> getStatsForCryptoBetween(
      @Parameter(description = "Crypto symbol (e.g. BTC, ETH)") @PathVariable String symbol,
      @Parameter(description = "Start date (optional)") @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @Parameter(description = "End date (optional)") @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

  @Operation(summary = "Get crypto with highest normalized range for a specific date", description = "Requires a date parameter. Returns the crypto with the highest normalized range on that date.", responses = {
      @ApiResponse(responseCode = "200", description = "Top crypto successfully returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CryptoNormalizedRange.class), examples = @ExampleObject(name = "Top Normalized Range", summary = "Example response", value = """
          {
            "symbol": "DOGE",
            "normalizedRange": 0.040272263187748125,
            "oldestTimestamp": "2022-01-16T00:00:00Z",
            "newestTimestamp": "2022-01-17T00:00:00Z"
          }
          """))),
      @ApiResponse(responseCode = "400", description = "Missing or invalid request parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Missing Date Param", summary = "Example of missing date param", value = """
          {
            "timestamp": "2025-04-18T15:30:43.445759036Z",
            "status": 400,
            "error": "Bad Request",
            "message": "Required request parameter 'date' is missing"
          }
          """))),
      @ApiResponse(responseCode = "404", description = "No data available for the specified date", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "No Data Found", summary = "No data available on date", value = """
          {
            "timestamp": "2025-04-18T18:50:00Z",
            "status": 404,
            "error": "Not Found",
            "message": "No crypto data available for 2022-05-31"
          }
          """)))
  })
  ResponseEntity<CryptoNormalizedRange> getTopCryptoForDay(
      @Parameter(description = "Date to analyze") @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

  @Operation(summary = "Get all cryptos sorted by normalized range descending", description = "Returns all cryptos sorted by normalized range. Optionally filter by start and end dates.", responses = {
      @ApiResponse(responseCode = "200", description = "List of cryptos (possibly empty) sorted by normalized range", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CryptoNormalizedRange.class)), examples = {
          @ExampleObject(name = "With Data", summary = "Example list of cryptos", value = """
              [
                {
                  "symbol": "BTC",
                  "normalizedRange": 0.3289,
                  "oldestTimestamp": "2022-01-01T00:00:00Z",
                  "newestTimestamp": "2022-01-31T00:00:00Z"
                },
                {
                  "symbol": "ETH",
                  "normalizedRange": 0.2761,
                  "oldestTimestamp": "2022-01-01T00:00:00Z",
                  "newestTimestamp": "2022-01-31T00:00:00Z"
                }
              ]
              """),
          @ExampleObject(name = "Empty List", summary = "No data found between dates", value = "[]")
      })),
      @ApiResponse(responseCode = "400", description = "Missing or invalid date filters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Missing Dates", summary = "Both startDate and endDate must be provided", value = """
          {
            "timestamp": "2025-04-18T20:45:43.000Z",
            "status": 400,
            "error": "Bad Request",
            "message": "Both startDate and endDate must be provided"
          }
          """)))
  })
  ResponseEntity<List<CryptoNormalizedRange>> getAllSortedByNormalizedRangeBetween(
      @Parameter(description = "Start date (optional)") @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @Parameter(description = "End date (optional)") @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);
}
