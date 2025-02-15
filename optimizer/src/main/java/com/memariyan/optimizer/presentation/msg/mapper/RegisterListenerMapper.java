package com.memariyan.optimizer.presentation.msg.mapper;

import com.memariyan.optimizer.api.msg.dto.VehicleCategoryDTO;
import com.memariyan.optimizer.api.msg.request.OptimizationRequest;
import com.memariyan.optimizer.domain.VehicleCategory;
import com.memariyan.optimizer.service.flow.model.OptimizationTaskModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterListenerMapper {

    OptimizationTaskModel toModel(OptimizationRequest request, String replyReceiverId);

    VehicleCategory toVehicleCategory(VehicleCategoryDTO category);

}
