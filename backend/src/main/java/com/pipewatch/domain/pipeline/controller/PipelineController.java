package com.pipewatch.domain.pipeline.controller;

import com.pipewatch.domain.pipeline.model.dto.PipelineResponse;
import com.pipewatch.global.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.pipewatch.global.statusCode.SuccessCode.*;

@RestController
@RequestMapping("${api_prefix}/pipelines")
@RequiredArgsConstructor
public class PipelineController {
	@GetMapping("/{pipelineUuid}")
	public ResponseEntity<?> pipelineDetail(@PathVariable String pipelineUuid) {
		PipelineResponse.PropertyDto property = new PipelineResponse.PropertyDto("Aluminum", 150.0, 10.3, "Water", 1.0);
		PipelineResponse.DefectDto defect1 = new PipelineResponse.DefectDto(new PipelineResponse.PositionDto(10.12, 22.11, 31.49), "CRACK");
		PipelineResponse.DefectDto defect2 = new PipelineResponse.DefectDto(new PipelineResponse.PositionDto(230.12, 122.11, 341.49), "CORROSION");

		PipelineResponse.DetailDto responseDto = PipelineResponse.DetailDto.builder()
				.name("Pipeline1")
				.updatedAt(LocalDateTime.now())
				.property(property)
				.defects(List.of(defect1, defect2))
				.build();

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

}
