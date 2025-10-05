package poke.consumer.dto;

import java.util.List;

public record SparseMatrixMessage(List<CellCount> cells) {}
