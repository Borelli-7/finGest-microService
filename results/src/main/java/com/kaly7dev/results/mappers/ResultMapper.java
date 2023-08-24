package com.kaly7dev.results.mappers;

import com.kaly7dev.results.dtos.ResultDto;
import com.kaly7dev.results.entities.Result;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class ResultMapper {
    @Mapping(target = "resultID", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "weekNumber", ignore = true)
    public abstract Result mapToResult(ResultDto resultDto);

    public abstract ResultDto mapToDto(Result result);
    @Mapping(target = "resultID", ignore = true)
    public abstract Result mapResultToResult(Result result);

}
