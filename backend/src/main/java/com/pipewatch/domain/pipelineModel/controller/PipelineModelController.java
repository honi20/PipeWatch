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

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/models")
@RequiredArgsConstructor
public class PipelineModelController implements PipelineModelApiSwagger {
	private final PipelineModelService pipelineModelService;

	@PostMapping(value = "/upload-file", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> fileUpload(@AuthenticationPrincipal Long userId, @RequestPart(value = "file") MultipartFile file) throws IOException, ParseException {
		PipelineModelResponse.FileUploadDto responseDto = pipelineModelService.uploadFile(userId, file);

		return new ResponseEntity<>(ResponseDto.success(FILE_UPLOAD_AND_MODEL_CREATED, responseDto), HttpStatus.CREATED);
	}

	@PostMapping("/modeling")
	public ResponseEntity<?> modelingCreate(@RequestBody PipelineModelRequest.ModelingDto requestDto) throws IOException, ParseException {
		PipelineModelResponse.CreateModelingDto responseDto = pipelineModelService.createModeling(requestDto);

		return new ResponseEntity<>(ResponseDto.success(PIPELINE_MODELING_CREATED, responseDto), HttpStatus.CREATED);
	}

	@PatchMapping("/init/{modelId}")
	public ResponseEntity<?> modelInit(@AuthenticationPrincipal Long userId, @PathVariable Long modelId, @RequestBody PipelineModelRequest.InitDto requestDto) throws IOException, ParseException {
		pipelineModelService.initModel(userId, modelId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(MODEL_INIT_OK, null), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<?> modelList(@AuthenticationPrincipal Long userId, @RequestParam(required = false) String building, @RequestParam(required = false) Integer floor) {
		PipelineModelResponse.ListDto responseDto = pipelineModelService.getModelList(userId, building, floor);

		return new ResponseEntity<>(ResponseDto.success(MODEL_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/{modelId}")
	public ResponseEntity<?> modelDetail(@AuthenticationPrincipal Long userId, @PathVariable Long modelId) {
		PipelineModelResponse.DetailDto responseDto = pipelineModelService.getModelDetail(userId, modelId);

		return new ResponseEntity<>(ResponseDto.success(MODEL_DETAIL_OK, responseDto), HttpStatus.OK);
	}

	@PatchMapping("/{modelId}")
	public ResponseEntity<?> modelModify(@AuthenticationPrincipal Long userId, @PathVariable Long modelId, @RequestBody PipelineModelRequest.ModifyDto requestDto) {
		pipelineModelService.modifyModel(userId, modelId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(MODEL_MODIFIED_OK, null), HttpStatus.OK);
	}

	@DeleteMapping("/{modelId}")
	public ResponseEntity<?> modelDelete(@AuthenticationPrincipal Long userId, @PathVariable Long modelId) {
		pipelineModelService.deleteModel(userId, modelId);

		return new ResponseEntity<>(ResponseDto.success(MODEL_DELETED, null), HttpStatus.NO_CONTENT);
	}

	@GetMapping("/memos/{modelId}")
	public ResponseEntity<?> modelMemoList(@AuthenticationPrincipal Long userId, @PathVariable Long modelId) {
		PipelineModelResponse.MemoListDto responseDto = pipelineModelService.getModelMemoList(userId, modelId);

		return new ResponseEntity<>(ResponseDto.success(MODEL_MEMO_LIST_OK, responseDto), HttpStatus.OK);
	}

	@PostMapping("/memos/{modelId}")
	public ResponseEntity<?> modelMemoCreate(@AuthenticationPrincipal Long userId, @PathVariable Long modelId, @RequestBody PipelineModelRequest.MemoDto requestDto) {
		PipelineModelResponse.MemoListDto responseDto = pipelineModelService.createModelMemo(userId, modelId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(MODEL_MEMO_CREATED, responseDto), HttpStatus.OK);
	}

	@DeleteMapping("/memos/{memoId}")
	public ResponseEntity<?> modelMemoCreate(@AuthenticationPrincipal Long userId, @PathVariable Long memoId) {
		pipelineModelService.deleteModelMemo(userId, memoId);

		return new ResponseEntity<>(ResponseDto.success(MODEL_MEMO_DELETED, null), HttpStatus.OK);
	}
}
