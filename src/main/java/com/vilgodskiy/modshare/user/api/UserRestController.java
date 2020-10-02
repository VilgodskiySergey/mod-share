package com.vilgodskiy.modshare.user.api;

import com.vilgodskiy.modshare.application.config.security.ActiveUserHolder;
import com.vilgodskiy.modshare.user.domain.Role;
import com.vilgodskiy.modshare.user.domain.User;
import com.vilgodskiy.modshare.user.repository.UserRepository;
import com.vilgodskiy.modshare.user.repository.UserSpecificationFactory;
import com.vilgodskiy.modshare.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author Vilgodskiy Sergey 30.07.2020
 */
@RestController
@RequestMapping("/" + User.PATH)
@Api(tags = "Пользователь")
@RequiredArgsConstructor
public class UserRestController {

    private final UserAssembler userAssembler;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Атрибутивный поиск в списке объектов")
    public ResponseEntity<Page<UserResponse>> filter(
            @ApiParam(value = "Поисковая строка") @RequestParam(required = false) String searchString,
            @ApiParam(value = "Роль") @RequestParam(required = false) Role role,
            Pageable pageable) {
        return ResponseEntity.ok(userRepository
                .findAll(UserSpecificationFactory.filter(role, searchString), pageable)
                .map(userAssembler::assemble));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Получить объект по ID")
    public ResponseEntity<UserResponse> get(@ApiParam("Идентификатор объекта") @PathVariable UUID id) {
        return ResponseEntity.ok(userAssembler.assemble(userRepository.getOrThrow(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Создать объект")
    public ResponseEntity<UserResponse> create(
            @ApiParam("Новый объект") @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userAssembler.assemble(
                userService.create(request.getFirstName(), request.getLastName(), request.getMiddleName(),
                        request.getEmail(), request.getPhone(), request.getLogin(), request.getPassword(),
                        request.getRole(), ActiveUserHolder.getActiveUser())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Редактировать объект")
    public ResponseEntity<UserResponse> update(
            @ApiParam("Идентификатор объекта") @PathVariable UUID id,
            @ApiParam("Обновлённый объект") @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userAssembler.assemble(
                userService.update(id, request.getFirstName(), request.getLastName(), request.getMiddleName(),
                        request.getPhone(), ActiveUserHolder.getActiveUser())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Удалить объект")
    public ResponseEntity<Void> delete(@ApiParam("Идентификатор объекта") @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}