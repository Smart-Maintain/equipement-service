package com.smartmaintain.equipementservice.dto;

import java.util.UUID;

public record PieceRequestDto(
    UUID tacheId,
    UUID pieceId,
    int quantite,
    String requestedBy
) {}
