package com.chakray.usersapi.util;

import com.chakray.usersapi.dto.AddressDTO;
import com.chakray.usersapi.dto.UserResponseDTO;
import com.chakray.usersapi.model.User;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    private static final ZoneId MADAGASCAR_ZONE = ZoneId.of("Indian/Antananarivo");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static UserResponseDTO toDTO(User user) {

        String formattedDate = user.getCreatedAt()
                .atZone(MADAGASCAR_ZONE)
                .format(FORMATTER);

        List<AddressDTO> addresses = user.getAddresses()
                .stream()
                .map(a -> new AddressDTO(
                        a.getId(),
                        a.getName(),
                        a.getStreet(),
                        a.getCountryCode()
                ))
                .collect(Collectors.toList());

        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getTaxId(),
                formattedDate,
                addresses
        );
    }
}
