package com.expenshare.controller;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ExpenseSettlementsDto;
import com.expenshare.model.enums.Status;
import com.expenshare.service.Settlement.SettlementService;
import com.expenshare.service.expense.ExpenseService;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;

@Controller("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    private final SettlementService settlementService;

    public ExpenseController(
            ExpenseService expenseService,
            SettlementService settlementService
    ) {
        this.expenseService = expenseService;
        this.settlementService = settlementService;
    }

    @Operation(
        summary = "Add Expense",
        description = "Creates a new expense in the specified group. Supports EQUAL, EXACT, and PERCENT split types."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Expense created successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ExpenseDto.class),
                examples = @ExampleObject(
                    value = "{ \"expenseId\": 100, \"groupId\": 10, \"paidBy\": 1, \"amount\": 300.00, \"description\": \"Dinner\", \"split\": [ { \"userId\": 1, \"share\": -200.00 }, { \"userId\": 2, \"share\": 100.00 }, { \"userId\": 3, \"share\": 100.00 } ], \"createdAt\": \"2025-09-27T15:05:00Z\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"VALIDATION_ERROR\", \"message\": \"Split percentages must total 100\" }"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Group not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{ \"errorCode\": \"NOT_FOUND\", \"message\": \"Group not found\" }"
                )
            )
        )
    })
    @Post
    public HttpResponse<ExpenseDto> addExpense(@Body @Valid CreateExpenseRequest createExpenseRequest) {
        final ExpenseDto expenseDto = expenseService.addExpense(createExpenseRequest);
        return HttpResponse.created(expenseDto);
    }

    @Get("/{expenseId}/settlements")
    public HttpResponse<ExpenseSettlementsDto> getExpenseSettlements(
        @PathVariable Long expenseId,
        @QueryValue(value = "status") @Nullable Status status,
        @QueryValue(value = "fromUserId") @Nullable Long fromUserId,
        @QueryValue(value = "toUserId") @Nullable Long toUserId,
        @QueryValue(value = "page", defaultValue = "0") int page,
        @QueryValue(value = "size", defaultValue = "20") @Max(100) int size
    ) {
        ExpenseSettlementsDto settlements = settlementService
                .filterExpenseSettlements(expenseId, status, fromUserId, toUserId, page, size);
        return HttpResponse.ok(settlements);
    }
}
