package com.expenshare;

import com.expenshare.model.dto.expense.CreateExpenseRequest;
import com.expenshare.model.dto.expense.ExpenseDto;
import com.expenshare.model.dto.expense.ShareDto;
import com.expenshare.model.dto.group.CreateGroupRequest;
import com.expenshare.model.dto.group.GroupDto;
import com.expenshare.model.dto.user.CreateUserRequest;
import com.expenshare.model.entity.SplitType;
import com.expenshare.service.GroupService;
import com.expenshare.service.UserService;
import com.expenshare.service.expense.ExpenseService;
import com.expenshare.exception.NotFoundException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false, environments = "test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExpenseServiceTest {
    @Inject
    ExpenseService expenseService;

    @Inject
    UserService userService;

    @Inject
    GroupService groupService;

    Long groupId;
    Long user1Id;
    Long user2Id;
    Long user3Id;

    @BeforeAll
    void setupData() {
        CreateUserRequest req1 = new CreateUserRequest();
        req1.setName("User1");
        req1.setEmail("user1@ex.com");
        req1.setMobileNumber("111");
        user1Id = userService.createUser(req1).getId();

        CreateUserRequest req2 = new CreateUserRequest();
        req2.setName("User2");
        req2.setEmail("user2@ex.com");
        req2.setMobileNumber("222");
        user2Id = userService.createUser(req2).getId();

        CreateUserRequest req3 = new CreateUserRequest();
        req3.setName("User3");
        req3.setEmail("user3@ex.com");
        req3.setMobileNumber("333");
        user3Id = userService.createUser(req3).getId();

        CreateGroupRequest groupReq = new CreateGroupRequest();
        groupReq.setName("Test Group");
        groupReq.setMembers(Arrays.asList(user1Id, user2Id, user3Id));
        GroupDto group = groupService.createGroup(groupReq);
        groupId = group.getGroupId();
    }

    @Test
    void testAddExpenseSuccess() {
        CreateExpenseRequest req = new CreateExpenseRequest();
        req.setGroupId(groupId);
        req.setPaidBy(user1Id);
        req.setAmount(BigDecimal.valueOf(90));
        req.setDescription("Dinner");
        req.setSplitType(SplitType.EQUAL);

        ExpenseDto expense = expenseService.addExpense(req);

        assertNotNull(expense.getExpenseId());
        assertEquals(groupId, expense.getGroupId());
        assertEquals(BigDecimal.valueOf(90), expense.getAmount());
    }

    @Test
    void testAddExpenseGroupNotFound() {
        CreateExpenseRequest req = new CreateExpenseRequest();
        req.setGroupId(9999L);
        req.setPaidBy(user1Id);
        req.setAmount(BigDecimal.valueOf(50));
        req.setSplitType(SplitType.EQUAL);

        assertThrows(NotFoundException.class, () -> expenseService.addExpense(req));
    }

    @Test
    void testAddExpenseParticipantNotInGroup() {
        CreateUserRequest req = new CreateUserRequest();
        req.setName("Stranger");
        req.setEmail("stranger@ex.com");
        req.setMobileNumber("444");
        Long strangerId = userService.createUser(req).getId();

        CreateExpenseRequest expenseReq = new CreateExpenseRequest();
        expenseReq.setGroupId(groupId);
        expenseReq.setPaidBy(user1Id);
        expenseReq.setAmount(BigDecimal.valueOf(60));
        expenseReq.setParticipants(List.of(strangerId));
        expenseReq.setSplitType(SplitType.EQUAL);

        assertThrows(NotFoundException.class, () -> expenseService.addExpense(expenseReq));
    }

    @Test
    void testGetGroupBalances() {
        ShareDto balances = expenseService.getGroupBalances(groupId, null);
        assertEquals(groupId, balances.getGroupId());
        assertNotNull(balances.getBalances());
        // Optionally check balances for specific users
    }
}