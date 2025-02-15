package com.memariyan.optimizer.client.osrm;

import com.memariyan.components.common.exception.ClientException;
import com.memariyan.optimizer.client.osrm.model.response.DistanceMatrixResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "osrmClient", url = "${osrm.client.base-url}")
public interface OsrmClient {

    @RequestMapping(method = RequestMethod.GET, value = "/table/v1/driving/{locations}?annotations={annotations}")
    DistanceMatrixResponse getDistanceMatrix(@PathVariable String locations, @RequestParam String annotations) throws ClientException;

}
