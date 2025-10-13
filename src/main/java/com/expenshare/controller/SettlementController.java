package com.expenshare.controller;

import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.service.SettlementService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.validation.Valid;

@Controller("/api/settlements")
public class SettlementController {
     private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Post
    public HttpResponse<SettlementDto> createSettlement(@Body @Valid CreateSettlementRequest createSettlementRequest) {
        SettlementDto settlementDto = settlementService.createSettlement(createSettlementRequest);
        return HttpResponse.created(settlementDto);
    }
}
