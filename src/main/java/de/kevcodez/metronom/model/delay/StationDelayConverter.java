/**
 * MIT License
 * 
 * Copyright (c) 2016 Kevin Grüneberg
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
package de.kevcodez.metronom.model.delay;

import com.fasterxml.jackson.databind.JsonNode;

import de.kevcodez.metronom.model.stop.Station;
import de.kevcodez.metronom.model.stop.StationProvider;

import java.time.LocalTime;

import javax.inject.Inject;

/**
 * Converts the JSON response from Metronom to a {@link StationDelay}.
 * 
 * @author Kevin Grüneberg
 *
 */
public class StationDelayConverter {

  @Inject
  private StationProvider stationProvider;

  public StationDelay convert(Station station, JsonNode node) {
    String timeAsString = node.get("stand").asText();

    JsonNode nodeDeparture = node.get("abfahrt");

    StationDelay stationDelay = new StationDelay(station, LocalTime.parse(timeAsString));

    for (JsonNode singleDeparture : nodeDeparture) {
      String time = singleDeparture.get("zeit").asText();
      String train = singleDeparture.get("zug").asText();
      String targetStationName = singleDeparture.get("ziel").asText();
      int delayInMinutes = singleDeparture.get("prognosemin").asInt();

      Station targetStation = stationProvider.findStationByName(targetStationName);
      DelayedDeparture delayedDeparture = new DelayedDeparture(train,
        targetStation, LocalTime.parse(time), delayInMinutes);

      stationDelay.getDepartures().add(delayedDeparture);
    }

    return stationDelay;
  }

}
