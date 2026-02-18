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

        users.add(new User(
                UUID.randomUUID(),
                "user1@mail.com",//correo
                "user1",//nombre
                "+1 55 555 555 55",//telefono
                "6XtYu7jcgLHDh4euISiB0w==", // example_1
                "AARR990101XXX",//Tax_Id
                now,//Fecha de creación
                List.of(//Direcciones
                        new Address(1, "workaddress", "street No. 1", "UK"),
                        new Address(2, "homeaddress", "street No. 2", "AU")
                        )
                )
        );

        users.add(new User(
                UUID.randomUUID(),
                "user2@mail.com",
                "user2",
                "+1 55 555 555 56",
                "OVWI1+/iFqUWL/9SnRRJXw==", //example_2
                "BBRR990101YYY", 
                now,
                new ArrayList<>()
                )
        );

        users.add(new User(
                UUID.randomUUID(),
                "user3@mail.com",
                "user3",
                "+1 55 555 555 57",
                "5YYrHfitB3VFs0FQdzxx0g==", //example_3
                "CCRR990101ZZZ",
                now,
                new ArrayList<>()
                )
        );
    }

    public List<User> findAll() {
        return users;
    }

    /*      Funciones de creación de usuario        */

    public void save(User user) {
        users.add(user);
        /*
        Para comprobar que se encripto la contraseña se puede imprimir el dato
        NO RECOMENDADO PARA PRODUCCION
        */
        //System.out.println("Encrypted password: " + user.getPassword());
    }

    //Revisar registro previo del RFC
    public boolean existsByTaxId(String taxId) {
        return users.stream()
            .anyMatch(u -> u.getTaxId().equals(taxId));
    }
    
        /*      Funciones de login       */
    public User findByTaxId(String taxId) {
    return users.stream()
            .filter(u -> u.getTaxId().equals(taxId))
            .findFirst()
            .orElse(null);
    }

    /*          Funcion de actualización         */
    public User findById(UUID id) {
    return users.stream()
            .filter(u -> u.getId().equals(id))
            .findFirst()
            .orElse(null);
    }


}
