package by.project.turamyzba.controllers;

import by.project.turamyzba.entities.Address;
import by.project.turamyzba.services.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@Tag(name = "Address", description = "Операции над адресами")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/get-children/{parentId}")
    @Operation(
            summary = "Получить дочерние адреса",
            description = "Возвращает список всех адресов (Address), у которых поле parentid совпадает с переданным parentId.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно, возвращает список адресов"),
                    @ApiResponse(responseCode = "404", description = "Если ни одного дочернего адреса с таким parentId не найдено"),
                    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
            }
    )
    public List<Address> getChildren(
            @Schema(description = "ID родительского адреса")
            @PathVariable Long parentId
    ) {
        return addressService.getChildrenByParentId(parentId);
    }
}
