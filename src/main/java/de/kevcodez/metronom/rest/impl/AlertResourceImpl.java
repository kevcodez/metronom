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
package de.kevcodez.metronom.rest.impl;

import static java.util.stream.Collectors.toList;

import de.kevcodez.metronom.model.alert.Alert;
import de.kevcodez.metronom.model.alert.AlertCache;
import de.kevcodez.metronom.rest.AlertResource;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

/**
 * Implements {@link AlertResource}.
 * 
 * @author Kevin Grüneberg
 *
 */
public class AlertResourceImpl implements AlertResource {

  @Inject
  private AlertCache alertCache;

  @Override
  public List<Alert> findAllAlerts() {
    return alertCache.getAlerts();
  }

  @Override
  public List<Alert> findAlertsSince(@PathParam(value = "since") String dateTime) {
    LocalDateTime localDateTime = LocalDateTime.parse(dateTime);

    if (localDateTime != null) {
      return streamAlerts().filter(d -> d.getCreationDate().isAfter(localDateTime))
        .collect(toList());
    }

    return Collections.emptyList();
  }

  @Override
  public List<Alert> findByText(@PathParam(value = "text") String text) {
    return streamAlerts().filter(d -> d.getMessage().contains(text)).collect(toList());
  }

  private Stream<Alert> streamAlerts() {
    return alertCache.getAlerts().stream();
  }

}
