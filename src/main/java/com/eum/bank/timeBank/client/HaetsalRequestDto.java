package com.eum.bank.timeBank.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class HaetsalRequestDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class AccountNumberList{
        private List<String> accountNumberList;
    }
}
