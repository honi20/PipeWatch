package com.pipewatch.domain.pipeline.controller;

import com.pipewatch.domain.pipeline.model.dto.PipelineRequest;
import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.domain.pipeline.service.PipelineService;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/pipelines")
@RequiredArgsConstructor
public class PipelineController implements PipelineApiSwagger {
	private final PipelineService pipelineService;

	@GetMapping("/{pipelineId}")
	public ResponseEntity<?> pipelineDetail(@AuthenticationPrincipal Long userId, @PathVariable Long pipelineId) {
		PipelineResponse.DetailDto responseDto = pipelineService.getPipelineDetail(userId, pipelineId);

		return new ResponseEntity<>(ResponseDto.success(PIPELINE_DETAIL_OK, responseDto), HttpStatus.OK);
	}

	@PutMapping("/{pipelineId}")
	public ResponseEntity<?> pipelineModify(@AuthenticationPrincipal Long userId, @PathVariable Long pipelineId, PipelineRequest.ModifyDto requestDto) {
		pipelineService.modifyPipeline(userId, pipelineId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(PIPELINE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PutMapping("/{pipelineId}/property")
	public ResponseEntity<?> pipelinePropertyModify(@AuthenticationPrincipal Long userId, @PathVariable Long pipelineId, PipelineRequest.ModifyPropertyDto requestDto) {
		pipelineService.modifyPipelinePropery(userId, pipelineId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(PIPELINE_PROPERTY_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PostMapping("/pipes/{pipeId}")
	public ResponseEntity<?> pipeMemoCreate(@AuthenticationPrincipal Long userId, @PathVariable Long pipeId, PipelineRequest.CreateMemoDto requestDto) {
		PipelineResponse.MemoListDto responseDto = pipelineService.createPipeMemo(userId, pipeId, requestDto);

		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_CREATED, responseDto), HttpStatus.CREATED);
	}

	@GetMapping("/{pipelineId}/memo")
	public ResponseEntity<?> pipelineMemoList(@AuthenticationPrincipal Long userId, @PathVariable Long pipelineId) {
		PipelineResponse.PipelineMemoListDto responseDto = pipelineService.getPipelineMemoList(userId, pipelineId);

		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_LIST_OK, responseDto), HttpStatus.OK);
	}

	@GetMapping("/pipes/{pipeId}")
	public ResponseEntity<?> pipeMemoList(@AuthenticationPrincipal Long userId, @PathVariable Long pipeId) {
		PipelineResponse.MemoListDto responseDto = pipelineService.getPipeMemoList(userId, pipeId);

		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_LIST_OK, responseDto), HttpStatus.OK);
	}

	@DeleteMapping("/pipes/{memoId}")
	public ResponseEntity<?> pipeMemoDelete(@AuthenticationPrincipal Long userId, @PathVariable Long memoId) {
		pipelineService.deletePipeMemo(userId, memoId);

		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_DELETED, null), HttpStatus.NO_CONTENT);
	}

}
