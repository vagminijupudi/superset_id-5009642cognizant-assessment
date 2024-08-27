package com.bookstore.api.BookstoreAPI.Book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BookMapper {
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);
    
    @Mapping(target = "add",ignore = true)
    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
}
