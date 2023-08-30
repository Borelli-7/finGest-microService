package com.kaly7dev.balances.services;

import com.kaly7dev.balances.dtos.BalanceDto;
import com.kaly7dev.balances.entities.Balance;
import com.kaly7dev.balances.exceptions.BalanceNotFoundException;
import com.kaly7dev.balances.functions.TriFunction;
import com.kaly7dev.balances.mappers.BalanceMapper;
import com.kaly7dev.balances.repositories.BalanceRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@Slf4j
@AllArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final BalanceMapper balanceMapper;
    private final BalanceRepo balanceRepo;
    private static final String ID_NOT_FOUND= "Balance ID not found In DataBase !";
    private static final String SUCCESS= "Balance Successfully ";
    @Override
    @Transactional
    public BalanceDto createBalance(BalanceDto balanceDto) {
        Function<BalanceDto, Balance> balanceToSave= bDto->{
            Balance b2= balanceMapper.mapToBalance(bDto);
            b2.setCreatedDate(Instant.now());
            return b2;
        };
        Function<Function<BalanceDto, Balance>, BalanceDto > balanceDtoCreated= balance->
                balanceMapper.mapToDto(
                        balanceRepo.save(
                                balance.apply(balanceDto)));

        BalanceDto created= balanceDtoCreated.apply(balanceToSave);

        log.info(SUCCESS+"Created!");
        return created;
    }

    @Override
    @Transactional
    public BalanceDto updateBalance(BalanceDto balanceDto) {
        Function<Function<Long, Balance>, Balance> updateFoundBalance = getUpdateFoundBalance(balanceDto);
        Function<Balance, BalanceDto> saveUpdatedBalance = getSaveUpdatedBalance();

        BiFunction< Function<Function<Long,Balance>, Balance>, Function<Balance, BalanceDto>, BalanceDto> balanceDtoUpdated=
                (funcFound,funcSaved)->{
                    BalanceDto updated= funcSaved.apply(
                            funcFound.apply(
                                    findById(balanceDto.balanceID())
                            )
                    );
                    log.info(SUCCESS+"Updated !");
                    return  updated;
        };
        return balanceDtoUpdated.apply(updateFoundBalance,saveUpdatedBalance);
    }

    @Override
    @Transactional
    public String deleteBalance(Long balanceID) {
        Function<Long,Balance> foundBalance= findById(balanceID);
        Function<Function<Long, Balance>, String> deleteBalance = getDeleteBalance(balanceID);

        return deleteBalance.apply(foundBalance);
    }

    @Override
    @Transactional(readOnly = true)
    public BalanceDto getBalanceById(Long balanceID) {
        Function<Long,Balance> foundBalance= findById(balanceID);
        Function<Function<Long, Balance>, BalanceDto> getBalanceDto= getDto(balanceID);

        return getBalanceDto.apply(foundBalance);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> listBalances(boolean assets,
                                            String desc,
                                            double price,
                                            int page,
                                            int size) {

        Pageable paging= PageRequest.of(page, size);

        TriFunction<String, Double, Pageable, Page<Balance>> getPageBalanceList= getSearchFunction();

        List<Balance> balanceList= getPageBalanceList.apply(desc, price, paging).getContent();

        BiFunction<Boolean, List<Balance>, List<BalanceDto>> getBalanceTypeLists= getTypeLists();

        BiFunction<BiFunction<Boolean, List<Balance>, List<BalanceDto>>,
                TriFunction<String, Double, Pageable, Page<Balance>>, Map< String, Object>> getBalancePaginated=
                getPaginated(assets, desc, price, paging, balanceList);

        return getBalancePaginated.apply(getBalanceTypeLists, getPageBalanceList);
    }

    private static BiFunction<BiFunction<Boolean, List<Balance>, List<BalanceDto>>,
            TriFunction<String, Double, Pageable, Page<Balance>>,
            Map<String, Object>> getPaginated(boolean assets,
                                              String desc,
                                              Double price,
                                              Pageable paging,
                                              List<Balance> balanceList) {
        return (bDtoList, bPageList) -> {
            Map<String, Object> response = new HashMap<>();
            response.put("results List", bDtoList.apply(assets, balanceList));
            response.put("current Page", bPageList.apply(desc, price, paging).getNumber());
            response.put("total Items", bPageList.apply(desc, price, paging).getTotalElements());
            response.put("total Pages", bPageList.apply(desc, price, paging).getTotalPages());

            log.info("Balances list successfully paginated ! ");
            return response;
        };
    }

    private BiFunction<Boolean, List<Balance>, List<BalanceDto>> getTypeLists() {
        return (bType, bList) -> {
            if (Boolean.TRUE.equals(bType)) {
                return getDtosAssetsList(bList);
            } else {
                return getDtosLiabilitiesList(bList);
            }
        };
    }

    private List<BalanceDto> getDtosLiabilitiesList(List<Balance> bList) {
        List<BalanceDto> liabilitiesList= bList.parallelStream()
                .filter(balance -> !balance.isAssets())
                .map(balanceMapper::mapToDto)
                .toList();
        log.info("Balances Liabilities successfully listed ! ");
        return liabilitiesList;

    }

    private List<BalanceDto> getDtosAssetsList(List<Balance> bList) {
        List<BalanceDto> assetsList= bList.parallelStream()
                .filter(Balance::isAssets)
                .map(balanceMapper::mapToDto)
                .toList();
        log.info("Balances Assets successfully listed ! ");
        return assetsList;
    }

    private TriFunction<String, Double, Pageable, Page<Balance>> getSearchFunction() {
        return (bDesc, bPrice, pg)->{
            Page<Balance> pageBalanceLists;
            if ((bDesc != null) && (bPrice != 0) ){
                pageBalanceLists= balanceRepo.findByDescriptionAndPrice(bDesc, bPrice, pg);
                log.info("Search for balances by description and price carried out successfully ! ");

            }else if((bDesc == null) && (bPrice != 0)){
                pageBalanceLists= balanceRepo.findByPrice(bPrice, pg);
                log.info("Search for balances by price carried out successfully ! ");

            } else if (bDesc != null) {
                pageBalanceLists= balanceRepo.findByDescription(bDesc, pg);
                log.info("Search for balances by description carried out successfully ! ");

            }else {
                pageBalanceLists= balanceRepo.findAll(pg);
                log.info(" Simple Search for balances carried out successfully ! ");
            }
            return pageBalanceLists;
        };
    }

    private Function<Long,Balance> findById(Long id) {
        Function<Long,Function<Long,Balance>> findById;
        findById = iD-> bId-> balanceRepo.findById(bId)
                .orElseThrow(() -> new BalanceNotFoundException(ID_NOT_FOUND));
        return findById.apply(id);
    }

    private Function<Function<Long, Balance>, BalanceDto> getDto(Long balanceID) {
        return func -> {
            BalanceDto found = balanceMapper.mapToDto(func.apply(balanceID));

            log.info(SUCCESS + "Found !");
            return found;
        };
    }

    private Function<Balance, BalanceDto> getSaveUpdatedBalance() {
        return balance ->
                balanceMapper.mapToDto(
                        balanceRepo.save(balance)
                );
    }

    private Function<Function<Long, Balance>, Balance> getUpdateFoundBalance(BalanceDto balanceDto) {
        return func-> {
            Balance found= func.apply(balanceDto.balanceID());
            found.setAmount(balanceDto.amount());
            found.setPrice(balanceDto.price());
            found.setDescription(balanceDto.description());
            return found;
        };
    }
    private Function<Function<Long, Balance>, String> getDeleteBalance(Long balanceID) {
        return func ->{
            Balance toDeleted= func.apply(balanceID);
            balanceRepo.delete(toDeleted);
            log.info(SUCCESS+"Deleted !");
            return SUCCESS+"Deleted !";
        };
    }

}
