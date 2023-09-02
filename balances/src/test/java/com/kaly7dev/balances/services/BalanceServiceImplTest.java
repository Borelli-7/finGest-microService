package com.kaly7dev.balances.services;

import com.kaly7dev.balances.dtos.BalanceDto;
import com.kaly7dev.balances.entities.Balance;
import com.kaly7dev.balances.exceptions.BalanceNotFoundException;
import com.kaly7dev.balances.mappers.BalanceMapper;
import com.kaly7dev.balances.repositories.BalanceRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BalanceServiceImplTest {
    @Mock
    private BalanceMapper balanceMapper;
    @Mock
    private BalanceRepo balanceRepo;
    @Captor
    private ArgumentCaptor<Balance> balanceArgumentCaptor;
    private BalanceService balanceService;
    @BeforeEach
    void setUp() {
        balanceService= new BalanceServiceImpl(balanceMapper, balanceRepo) ;
    }

    @Test
    @DisplayName("Should create Balance")
    void testCreateBalance() {
        Balance balance= new Balance(
                1L, "balanceDesc", 1000, null, true, BigDecimal.valueOf(100000));
        BalanceDto balanceDto= new BalanceDto(
                1L, "balanceDesc", 1000, null, true, BigDecimal.valueOf(100000));

        Mockito.when(balanceMapper.mapToBalance(balanceDto))
                .thenReturn(balance);

        balanceService.createBalance(balanceDto);
        Mockito.verify(balanceRepo, Mockito.times(1))
                .save(balanceArgumentCaptor.capture());

        Assertions.assertThat(balanceArgumentCaptor.getValue().getBalanceID()).isEqualTo(1L);
        Assertions.assertThat(balanceArgumentCaptor.getValue().getPrice()).isEqualTo(1000D);
    }

    @Test
    @DisplayName("Should Retrieve Result by ID")
    void testGetBalanceById() {
        Balance balance= new Balance(
                1L, "balanceDesc", 1000, null, true, BigDecimal.valueOf(100000));
        BalanceDto expectedBalanceDto= new BalanceDto(
                1L, "balanceDesc", 1000, null, true, BigDecimal.valueOf(100000));

        Mockito.when(balanceRepo.findById(1L))
                .thenReturn(Optional.of(balance));
        Mockito.when(balanceMapper.mapToDto(Mockito.any(Balance.class)))
                .thenReturn(expectedBalanceDto);

        BalanceDto ActualBalaceDto= balanceService.getBalanceById(1L);

        Assertions.assertThat(ActualBalaceDto.balanceID())
                .isEqualTo(expectedBalanceDto.balanceID());
        Assertions.assertThat(ActualBalaceDto.price())
                .isEqualTo(expectedBalanceDto.price());

    }
    @Test
    @DisplayName("Should Throw Exception when Balance ID not exist in DB")
    void shouldFailWhenBalanceIDNotExist() {

        Assertions.assertThatThrownBy(
                        ()-> balanceService.getBalanceById(12L)
                ).isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance ID not found In DataBase !");
    }

    @Test
    @DisplayName("Should delete Balance in Database")
    void testDeleteBalance() {
        Balance balance= new Balance(
                1L, "balanceDesc", 1000, null, true, BigDecimal.valueOf(100000));
        Optional<Balance> OptionalBalance= Optional.of(balance);

        Mockito.when(balanceRepo.findById(1L))
                .thenReturn(OptionalBalance);

        balanceService.deleteBalance(1L);
        Mockito.verify(balanceRepo, Mockito.times(1))
                .delete(balanceArgumentCaptor.capture());
    }

    @Test
    @DisplayName("Should list only Assets")
    void testListOnlyAssetsBalances() {
        Balance balance1= new Balance(
                1L, "assetbalanceDesc", 1000.0, null, true, BigDecimal.valueOf(100000));

        BalanceDto balanceDto1= new BalanceDto(
                1L, "assetbalanceDesc", 1000.0, null, true, BigDecimal.valueOf(100000));


        List<Balance> balanceList= new ArrayList<>();
        balanceList.add(balance1);

        List<BalanceDto> expectedBalanceDtoList= new ArrayList<>();
        expectedBalanceDtoList.add(balanceDto1);

        Pageable paging= PageRequest.of(0, 3);
        Page<Balance> pageResultList= new PageImpl<>(balanceList);

        Mockito.when(balanceService.getSearchFunction().apply("assetbalanceDesc", 1000.0, paging))
                .thenReturn(pageResultList);
        Mockito.when(balanceMapper.mapToDto(Mockito.any(Balance.class)))
                .thenReturn(balanceDto1);

        Map<String, Object> actualAssetBalanceDtoList=
                balanceService.listBalances(
                        true,"assetbalanceDesc", 1000.0, 0,3);

        Assertions.assertThat(actualAssetBalanceDtoList).containsValue(expectedBalanceDtoList);
    }

    @Test
    @DisplayName("Should list only Liabilities")
    void testListOnlyLiabilitiesBalances() {
        Balance balance1= new Balance(
                1L, null, 0.0, null, false, BigDecimal.valueOf(100000));

        BalanceDto balanceDto1= new BalanceDto(
                1L, null, 0.0, null, false, BigDecimal.valueOf(100000));


        List<Balance> balanceList= new ArrayList<>();
        balanceList.add(balance1);

        List<BalanceDto> expectedBalanceDtoList= new ArrayList<>();
        expectedBalanceDtoList.add(balanceDto1);

        Pageable paging= PageRequest.of(0, 3);
        Page<Balance> pageResultList= new PageImpl<>(balanceList);

        Mockito.when(balanceService.getSearchFunction().apply(null, 0.0, paging))
                .thenReturn(pageResultList);
        Mockito.when(balanceMapper.mapToDto(Mockito.any(Balance.class)))
                .thenReturn(balanceDto1);

        Map<String, Object> actualAssetBalanceDtoList=
                balanceService.listBalances(
                        false,null, 0.0, 0,3);

        Assertions.assertThat(actualAssetBalanceDtoList).containsValue(expectedBalanceDtoList);
    }
}
