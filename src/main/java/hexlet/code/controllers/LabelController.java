package hexlet.code.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hexlet.code.dto.LabelDto;
import hexlet.code.dto.LabelResponseDto;
import hexlet.code.service.interfaces.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("${base-url}" + "labels")
public class LabelController {
    private static final String ID = "/{id}";
    public static final String LABEL_CONTROLLER_PATH = "labels";

    @Autowired
    private LabelService labelService;

    @Operation(summary = "Get all labels", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public List<LabelResponseDto> getAllLabels() {
        return this.labelService.getAllLabels()
                .stream()
                .map(LabelResponseDto::new)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get label by id", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(path = ID)
    public LabelResponseDto getlabel(@PathVariable("id")
    long id) {
        return this.labelService.getLabel(id)
                .map(LabelResponseDto::new)
                .orElseThrow();
    }

    @Operation(summary = "Create label", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponse(responseCode = "201")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public LabelResponseDto createLabel(@RequestBody @Valid
    LabelDto labelDto) {
        var label = this.labelService.createLabel(labelDto);
        return new LabelResponseDto(label);
    }

    @Operation(summary = "Update label", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping(path = ID)
    public LabelResponseDto updateLabel(@PathVariable("id")
    long id,
            @RequestBody @Valid
            LabelDto labelDto) {
        var label = this.labelService.updateLabel(id, labelDto);
        return new LabelResponseDto(label);
    }

    @Operation(summary = "Delete label", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteLabel(@PathVariable("id")
    long id) {
        this.labelService.deleteLabel(id);
    }
}
