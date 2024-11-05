package com.pipewatch.domain.pipelineModel.controller;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelRequest;
import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import com.pipewatch.domain.pipelineModel.service.PipelineModelService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/models")
@RequiredArgsConstructor
public class PipelineModelController implements PipelineModelApiSwagger {
	private final PipelineModelService pipelineModelService;

	@PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> fileUpload(@AuthenticationPrincipal Long userId, @RequestPart(value = "file", required = false) MultipartFile file) throws IOException, ParseException {
		PipelineModelResponse.FileUploadDto responseDto = pipelineModelService.uploadFile(userId, file);

		return new ResponseEntity<>(ResponseDto.success(FILE_UPLOAD_AND_MODEL_CREATED, responseDto), HttpStatus.CREATED);
	}

	@PostMapping("/modeling")
	public ResponseEntity<?> modelingCreate(@RequestBody PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
		PipelineModelResponse.CreateModelingDto responseDto = pipelineModelService.createModeling(requestDto);

		return new ResponseEntity<>(ResponseDto.success(PIPELINE_MODELING_CREATED, responseDto), HttpStatus.CREATED);
	}

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

	@DeleteMapping("/{modelId}")
	public ResponseEntity<?> modelDelete(@PathVariable Long modelId) {
		return new ResponseEntity<>(ResponseDto.success(MODEL_DELETED, null), HttpStatus.NO_CONTENT);
	}
}
