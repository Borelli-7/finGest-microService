package com.kaly7dev.balances.services;

import com.kaly7dev.balances.dtos.BalanceDto;
import com.kaly7dev.balances.entities.Balance;
import com.kaly7dev.balances.functions.TriFunction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    TriFunction<String, Double, Pageable, Page<Balance>> getSearchFunction();


}
