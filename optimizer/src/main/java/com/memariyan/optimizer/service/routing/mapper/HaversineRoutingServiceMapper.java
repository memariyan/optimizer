package com.memariyan.optimizer.service.routing.mapper;

import com.memariyan.optimizer.domain.Location;
import com.memariyan.optimizer.service.base.model.LocationModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HaversineRoutingServiceMapper {

    Location[] toLocation(LocationModel[] locations);

}
