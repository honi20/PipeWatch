package com.pipewatch.domain.pipeline.controller;

import com.pipewatch.domain.enterprise.model.dto.EnterpriseResponse;
import com.pipewatch.domain.pipeline.model.dto.PipelineModelResponse;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

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

	@PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> fileUpload(@RequestPart(value = "file", required = false) MultipartFile file) {
		PipelineModelResponse.FileUploadDto responseDto = PipelineModelResponse.FileUploadDto.builder()
				.modelId(1L)
				.build();

		return new ResponseEntity<>(ResponseDto.success(FILE_UPLOAD_AND_MODEL_CREATED, responseDto), HttpStatus.CREATED);
	}

	@PatchMapping("/init/{modelId}")
	public ResponseEntity<?> modelInit(@PathVariable Long modelId) {
		return new ResponseEntity<>(ResponseDto.success(MODEL_INIT_OK, null), HttpStatus.OK);
	}

	@PatchMapping("/{modelId}")
	public ResponseEntity<?> modelModify(@PathVariable Long modelId) {
		return new ResponseEntity<>(ResponseDto.success(MODEL_MODIFIED_OK, null), HttpStatus.OK);
	}

	@GetMapping("/{modelId}")
	public ResponseEntity<?> modelDetail(@PathVariable Long modelId) {
		PipelineModelResponse.DetailDto responseDto = PipelineModelResponse.DetailDto.builder()
				.name("파이프라인 모델")
				.description("주기적인 점검 필요")
				.modelingUrl("s3 url")
				.isCompleted(true)
				.updatedAt(LocalDateTime.now())
				.creator(new PipelineModelResponse.Creator(1L, "김싸피"))
				.build();

		return new ResponseEntity<>(ResponseDto.success(MODEL_DETAIL_OK, responseDto), HttpStatus.OK);
	}

}
