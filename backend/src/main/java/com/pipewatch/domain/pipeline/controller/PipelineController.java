package com.pipewatch.domain.pipeline.controller;

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

	@PutMapping("/{pipelineUuid}")
	public ResponseEntity<?> pipelineModify(@PathVariable String pipelineUuid) {
		return new ResponseEntity<>(ResponseDto.success(PIPELINE_MODIFIED_OK, null), HttpStatus.OK);
	}

	@PutMapping("/{pipelineUuid}/properties")
	public ResponseEntity<?> pipelinePropertyModify(@PathVariable String pipelineUuid) {
		return new ResponseEntity<>(ResponseDto.success(PIPELINE_PROPERTY_MODIFIED_OK, null), HttpStatus.OK);
	}

	@GetMapping("/pipes/{pipeUuid}")
	public ResponseEntity<?> pipeDetail(@PathVariable String pipeUuid) {
		PipelineResponse.MemoDto memo1 = new PipelineResponse.MemoDto(1L, "부식 위험 있음", new PipelineResponse.Creator(1L, "김싸피"), LocalDateTime.now());
		PipelineResponse.MemoDto memo2 = new PipelineResponse.MemoDto(2L, "다음주에 점검함", new PipelineResponse.Creator(2L, "최싸피"), LocalDateTime.now());

		PipelineResponse.MemoListDto responseDto = PipelineResponse.MemoListDto.builder()
				.memos(List.of(memo1, memo2))
				.build();

		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_LIST_OK, responseDto), HttpStatus.OK);
	}

	@PostMapping("/pipes/{pipeUuid}")
	public ResponseEntity<?> pipeMemoCreate(@PathVariable String pipeUuid) {
		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_CREATED, null), HttpStatus.CREATED);
	}

	@DeleteMapping("/pipes/{memoId}")
	public ResponseEntity<?> pipeMemoCreate(@PathVariable Long memoId) {
		return new ResponseEntity<>(ResponseDto.success(PIPE_MEMO_DELETED, null), HttpStatus.NO_CONTENT);
	}

}
