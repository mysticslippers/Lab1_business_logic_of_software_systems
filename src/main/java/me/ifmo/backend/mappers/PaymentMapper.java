package me.ifmo.backend.mappers;

import me.ifmo.backend.DTO.PaymentDTO;
import me.ifmo.backend.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "enrollment.id", target = "enrollmentId")
    PaymentDTO toDto(Payment payment);
}
