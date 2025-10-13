package com.expenshare.controller;

import com.expenshare.model.dto.settlement.CreateSettlementRequest;
import com.expenshare.model.dto.settlement.SettlementDto;
import com.expenshare.model.dto.settlement.SettlementStatusDto;
import com.expenshare.service.SettlementService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
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

    @Post("/{settlementId}/confirm")
    public HttpResponse<SettlementStatusDto> confirmSettlement(@PathVariable Long settlementId) {
        SettlementStatusDto statusDto = settlementService.confirmSettlement(settlementId);
        return HttpResponse.ok(statusDto);
    }

    @Post("/{settlementId}/cancel")
    public HttpResponse<SettlementStatusDto> cancelSettlement(@PathVariable Long settlementId) {
        SettlementStatusDto statusDto = settlementService.cancelSettlement(settlementId);
        return HttpResponse.ok(statusDto);
    }
}
