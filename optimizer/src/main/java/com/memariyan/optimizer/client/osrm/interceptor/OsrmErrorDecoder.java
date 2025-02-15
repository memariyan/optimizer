package com.memariyan.optimizer.client.osrm.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memariyan.components.common.exception.ClientException;
import com.memariyan.optimizer.client.osrm.model.response.BaseOsrmResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class OsrmErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            BaseOsrmResponse result = getBody(response);
            return new ClientException("osrm api error -> code : " + result.getCode() + ", message : " + result.getMessage(),
                    HttpStatus.resolve(response.status()));

        } catch (Exception e) {
            return new ClientException(response.reason(), HttpStatus.resolve(response.status()));
        }
    }

    private BaseOsrmResponse getBody(Response response) throws IOException {
        try (InputStream bodyIs = response.body().asInputStream()) {
            return objectMapper.readValue(bodyIs, BaseOsrmResponse.class);
        } catch (IOException e) {
            log.warn("osrm response json decoding error : ", e);
            throw e;
        }
    }
}
