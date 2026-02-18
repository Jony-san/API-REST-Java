package com.chakray.usersapi.service;

//importar usuarios y modelo
import com.chakray.usersapi.model.User;
import com.chakray.usersapi.repository.UserRepository;
import org.springframework.stereotype.Service;
//importar validaciones de usuario
import com.chakray.usersapi.dto.CreateUserRequest;
import com.chakray.usersapi.validation.PhoneValidator;
import com.chakray.usersapi.validation.RfcValidator;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.UUID;

//Encriptación de contraseña
import com.chakray.usersapi.util.AES;
import com.chakray.usersapi.util.AESEnv;

//Login
import com.chakray.usersapi.dto.LoginRequest;

//Actualizacion
import com.chakray.usersapi.dto.UpdateUserRequest;

//Manejo de errores
import com.chakray.usersapi.util.UserNotFoundException;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service//Componente de la capa de logica de negocio
public class UserService {

    private final UserRepository userRepository;

    //Para usar contraseñas con Hash
    //private final PasswordEncoder passwordEncoder;

    /*
    version con variables de entorno de sistema
    Se puede definir la variable temporal con:
    
    export AES_SECRET="12345678901234567890123456789012"    (MAC)
    set AES_SECRET=12345678901234567890123456789012         (cmd)
    $env:AES_SECRET="12345678901234567890123456789012"      (PowerShell)

    */
    private final AESEnv aesEnv;

    public UserService(UserRepository userRepository,  AESEnv aesEnv ) {
        this.userRepository = userRepository;
        this.aesEnv = aesEnv;
    }

    public List<User> getUsers(String sortedBy, String filter) {

        List<User> users = userRepository.findAll();

        // Aplicar primero el filtro
        if (filter != null && !filter.isBlank()) {
            users = applyFilter(users, filter);
        }

        // Ordenar
        if (sortedBy != null && !sortedBy.isBlank()) {
            users = applySorting(users, sortedBy);
        }

        return users;
    }

    private List<User> applySorting(List<User> users, String sortedBy) {

        Comparator<User> comparator = switch (sortedBy) {
            case "email" -> Comparator.comparing(User::getEmail);
            case "id" -> Comparator.comparing(User::getId);
            case "name" -> Comparator.comparing(User::getName);
            case "phone" -> Comparator.comparing(User::getPhone);
            case "tax_id" -> Comparator.comparing(User::getTaxId);
            case "created_at" -> Comparator.comparing(User::getCreatedAt);
            default -> null;
        };

        if (comparator == null) return users;

        return users.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<User> applyFilter(List<User> users, String filter) {

        // formato: field+operator+value
        // En caso de que el navegador interprete "+" por " "
        filter = filter.replace(" ", "+");
        String[] parts = filter.split("\\+");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid filter format");
        }

        String field = parts[0];
        String operator = parts[1];
        String value = parts[2];

        return users.stream()
                .filter(user -> matches(user, field, operator, value))
                .collect(Collectors.toList());
    }

    private boolean matches(User user, String field, String operator, String value) {

        //Tomar campo de busqueda
        String fieldValue = switch (field) {
            case "email" -> user.getEmail();
            case "name" -> user.getName();
            case "phone" -> user.getPhone();
            case "tax_id" -> user.getTaxId();
            case "created_at" -> user.getCreatedAt().toString();
            case "id" -> user.getId().toString();
            default -> null;
        };

        if (fieldValue == null) return false;
        //Aplicar operacion de busqueda
        return switch (operator) {
            case "co" -> fieldValue.contains(value);
            case "eq" -> fieldValue.equals(value);
            case "sw" -> fieldValue.startsWith(value);
            case "ew" -> fieldValue.endsWith(value);
            default -> false;
        };
    }

    public User createUser(CreateUserRequest request) {

        if (!RfcValidator.isValid(request.getTaxId())) {
            throw new IllegalArgumentException("Invalid RFC format");
        }

        if (!PhoneValidator.isValid(request.getPhone())) {
            throw new IllegalArgumentException("Invalid phone format");
        }

        if (userRepository.existsByTaxId(request.getTaxId())) {
            throw new IllegalArgumentException("tax_id must be unique");
        }

        ZoneId madagascarZone = ZoneId.of("Indian/Antananarivo");

        User user = new User(
                UUID.randomUUID(),
                request.getEmail(),
                request.getName(),
                request.getPhone(),
                //AES.encrypt(request.getPassword()),    //Sin variable de entorno(clave hardcodeada, no recomendado)
                aesEnv.encrypt(request.getPassword()),  //con variable de entorno
                ////passwordEncoder.encode(password)    //Hash
                request.getTaxId(),
                LocalDateTime.now(madagascarZone),
                List.of()
        );

        userRepository.save(user);

        return user;
    }

    public boolean login(LoginRequest request) {

        User user = userRepository.findByTaxId(request.getTaxId());

        if (user == null) {
            return false;
        }

        String decryptedPassword = aesEnv.decrypt(user.getPassword());

        return decryptedPassword.equals(request.getPassword());
    }

    //Consideracion de update: si uno falla, pero previos no, los previos se actualizaron, informar cuales no
    public User updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        //Actualizando datos acorde a lo que se envio
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        //Adicional Validar que cumpla con el formato
        if (request.getPhone() != null) {
            if (!PhoneValidator.isValid(request.getPhone())) {
                throw new IllegalArgumentException("Invalid phone format");
            }
            user.setPhone(request.getPhone());
        }

        //Adicional Validar existencia y que sea unico
        if (request.getTaxId() != null) {
            if (!RfcValidator.isValid(request.getTaxId())) {
                throw new IllegalArgumentException("Invalid RFC format");
            }

            if (!request.getTaxId().equals(user.getTaxId())
                    && userRepository.existsByTaxId(request.getTaxId())) {
                throw new IllegalArgumentException("tax_id must be unique");
            }

            user.setTaxId(request.getTaxId());
        }

        if (request.getPassword() != null) {
            user.setPassword(aesEnv.encrypt(request.getPassword()));
            //Corroborar contraseña al momento de actualizacion, NO RECOMENDADO EN PRODUCCION, SOLO ES PARA PRUEBAS de funcionamiento
            System.out.println("New Encrypted password: " + user.getPassword());

        }

        return user;
    }

    public void deleteUser(UUID id) {
    
        boolean deleted = userRepository.deleteById(id);
    
        if (!deleted) {
            throw new UserNotFoundException("User not found");
        }
    }


}
