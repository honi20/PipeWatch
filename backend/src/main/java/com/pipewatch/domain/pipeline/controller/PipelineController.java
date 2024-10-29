package com.pipewatch.domain.pipeline.controller;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.pipeline.model.dto.PipelineModelResponse;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.MODEL_LIST_OK;

@RestController
@RequestMapping("${api_prefix}/models")
@RequiredArgsConstructor
public class PipelineController {
	@GetMapping
	public ResponseEntity<?> modelList(@RequestParam(required = false) String building, @RequestParam(required = false) Integer floor) {
		PipelineModelResponse.PipelineModelDto model1 = new PipelineModelResponse.PipelineModelDto(1L, "model1", "previewUrl1", LocalDateTime.now());
		PipelineModelResponse.PipelineModelDto model2 = new PipelineModelResponse.PipelineModelDto(2L, "model2", "previewUrl2", LocalDateTime.now());
		PipelineModelResponse.PipelineModelDto model3 = new PipelineModelResponse.PipelineModelDto(3L, "model3", "previewUrl3", LocalDateTime.now());
		PipelineModelResponse.FloorListDto floorList1 = new PipelineModelResponse.FloorListDto(1, List.of(model1, model2));
		PipelineModelResponse.FloorListDto floorList2 = new PipelineModelResponse.FloorListDto(2, List.of(model3));
		PipelineModelResponse.BuildingListDto buildingList = new PipelineModelResponse.BuildingListDto("역삼 멀티캠퍼스", List.of(floorList1, floorList2));

		PipelineModelResponse.ListDto responseDto = PipelineModelResponse.ListDto.builder()
				.buildings(List.of(buildingList))
				.build();

		return new ResponseEntity<>(ResponseDto.success(MODEL_LIST_OK, responseDto), HttpStatus.OK);
	}
}
