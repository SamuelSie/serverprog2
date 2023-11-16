package com.yrgo.services.calls;

import com.yrgo.domain.Action;
import com.yrgo.domain.Call;
import com.yrgo.services.customers.CustomerManagementService;
import com.yrgo.services.customers.CustomerNotFoundException;
import com.yrgo.services.diary.DiaryManagementService;

import java.util.Collection;

public class CallHandlingServiceImpl implements CallHandlingService {
    CustomerManagementService customerManagementService;
    DiaryManagementService diaryManagementService;

    public CallHandlingServiceImpl(CustomerManagementService customer, DiaryManagementService diary) {
        this.customerManagementService = customer;
        this.diaryManagementService = diary;
    }

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions) throws CustomerNotFoundException {
        customerManagementService.recordCall(customerId, newCall);
        actions.forEach(action -> diaryManagementService.recordAction(action));
    }
}
