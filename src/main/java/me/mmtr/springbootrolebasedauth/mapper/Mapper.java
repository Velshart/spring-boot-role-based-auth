package me.mmtr.springbootrolebasedauth.mapper;

public interface Mapper<T, U> {
    T toDTO(U u);
    U toEntity(T dto);
}

