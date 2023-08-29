package com.kaly7dev.balances.services;

import com.kaly7dev.balances.dtos.BalanceDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface BalanceService {

    BalanceDto createBalance(BalanceDto balanceDto);
    BalanceDto updateBalance(BalanceDto balanceDto);
    String deleteBalance(Long balanceID);
    BalanceDto getBalanceById(Long balanceID);
    Map<String, Object> listBalances(boolean assets,
                                                     String desc,
                                                     double price,
                                                     int page,
                                                     int size);


}
