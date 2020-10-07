package com.vilgodskiy.modshare.mod.api;

import com.vilgodskiy.modshare.application.config.security.ActiveUserHolder;
import com.vilgodskiy.modshare.mod.domain.Mod;
import com.vilgodskiy.modshare.mod.repository.ModRepository;
import com.vilgodskiy.modshare.mod.repository.ModSpecificationFactory;
import com.vilgodskiy.modshare.mod.service.ModService;
import com.vilgodskiy.modshare.user.domain.Role;
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
 * @author Vilgodskiy Sergey 14.08.2020
 */
@RestController
@RequestMapping("/" + Mod.PATH)
@Api(tags = "Мод")
@RequiredArgsConstructor
public class ModRestController {

    private final ModAssembler modAssembler;
    private final ModService modService;
    private final ModRepository modRepository;
    private final ModModifyAccessChecker modModifyAccessChecker;

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Атрибутивный поиск в списке объектов")
    public ResponseEntity<Page<ModResponse>> filter(
            @ApiParam(value = "Фильтр") ModFilter filter, Pageable pageable) {
        return ResponseEntity.ok(modRepository
                .findAll(ModSpecificationFactory.filter(filter.getSearchString()), pageable).map(
                        modAssembler::assemble));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ApiOperation("Получить объект по ID")
    public ResponseEntity<ModResponse> get(@ApiParam("Идентификатор объекта") @PathVariable UUID id) {
        return ResponseEntity.ok(modAssembler.assemble(modRepository.getOrThrow(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + Role.MOD_DEVELOPER_ROLE + "')")
    @ApiOperation("Создать объект")
    public ResponseEntity<ModResponse> create(
            @ApiParam("Новый объект") @RequestBody ModCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modAssembler.assemble(
                modService.create(request.getTitle(), ActiveUserHolder.getActiveUser())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Role.MOD_DEVELOPER_ROLE + "')")
    @ApiOperation("Редактировать объект")
    public ResponseEntity<ModResponse> update(
            @ApiParam("Идентификатор объекта") @PathVariable UUID id,
            @ApiParam("Обновлённый объект") @RequestBody ModUpdateRequest request) {
        modModifyAccessChecker.checkAccessOrThrow(ActiveUserHolder.getActiveUser(), id);
        return ResponseEntity.ok(modAssembler.assemble(
                modService.update(id, request.getTitle(), ActiveUserHolder.getActiveUser())));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('" + Role.MOD_DEVELOPER_ROLE + ", " + Role.ADMINISTRATOR_ROLE + "')")
    @ApiOperation("Удалить объект")
    public ResponseEntity<Void> delete(@ApiParam("Идентификатор объекта") @PathVariable UUID id) {
        modModifyAccessChecker.checkAccessOrThrow(ActiveUserHolder.getActiveUser(), id);
        modService.delete(id);
        return ResponseEntity.noContent().build();
    }

}