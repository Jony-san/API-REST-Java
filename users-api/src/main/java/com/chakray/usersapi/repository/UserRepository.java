package com.chakray.usersapi.repository;

import com.chakray.usersapi.model.Address;
import com.chakray.usersapi.model.User;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository//Definicion de componente
public class UserRepository {
    //Guardar usuarios
    private final List<User> users = new ArrayList<>();

    @PostConstruct//Ejecución al iniciar proyecto
    public void init() {
        //Definir zona horaria para madagascar
        ZoneId madagascarZone = ZoneId.of("Indian/Antananarivo");
        //Transformar fecha local
        LocalDateTime now = LocalDateTime.now(madagascarZone);
        
        /* Crear usuarios locales */

        users.add(
                new User(
                        UUID.randomUUID(),
                        "user1@mail.com",//correo
                        "user1",//nombre
                        "+1 55 555 555 55",//telefono
                        "plaintext", // contraseña (despues se encripta)
                        "AARR990101XXX",//Tax_Id
                        now,//Fecha de creación
                        List.of(//Direcciones
                                new Address(1, "workaddress", "street No. 1", "UK"),
                                new Address(2, "homeaddress", "street No. 2", "AU")
                        )
                )
        );

        users.add(
                new User(
                        UUID.randomUUID(),
                        "user2@mail.com",
                        "user2",
                        "+1 55 555 555 56",
                        "plaintext",
                        "BBRR990101YYY",
                        now,
                        new ArrayList<>()
                )
        );

        users.add(
                new User(
                        UUID.randomUUID(),
                        "user3@mail.com",
                        "user3",
                        "+1 55 555 555 57",
                        "plaintext",
                        "CCRR990101ZZZ",
                        now,
                        new ArrayList<>()
                )
        );
    }

    public List<User> findAll() {
        return users;
    }

}
