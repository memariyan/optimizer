package com.memariyan.optimizer.domain;

import com.memariyan.optimizer.utils.SparseListModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Vehicle extends SparseListModel {

    private String categoryId;

    private Double weightCapacity;

    private Double volumeCapacity;

    private int cost;

}
