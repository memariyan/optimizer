package com.memariyan.optimizer.service.optimization.custom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class Path implements Cloneable, Comparable<Path> {

    private int distance;

    private List<Integer> visitOrders;

    @Override
    public Path clone() {
        return new Path(distance, new ArrayList<>(visitOrders));
    }

    @Override
    public int compareTo(@NotNull Path other) {
        return Integer.compare(distance, other.distance);
    }
}
