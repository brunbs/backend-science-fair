package com.school.science.fair.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
@JsonInclude(Include.NON_NULL)
@Schema(description = "ObjectErrorResponse")
public class ObjectErrorResponse {

  @Schema(description = "Mensagem de erro")
  private String message;

  @Schema(description = "Campo do erro")
  private String field;
}