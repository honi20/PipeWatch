package com.pipewatch.domain.pipelineModel.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "modelApi", url = "${AI_GPU_URL}")
public interface PipelineModelApiService {
	@PostMapping(value = "/inference", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	boolean transferImgFile(
			@RequestPart(value = "file") MultipartFile file,
			@RequestPart(value = "modelUuid") String modelUuid
	);

	@PostMapping(value = "/modeling", consumes = MediaType.TEXT_PLAIN_VALUE)
	void createModel(
			@RequestBody String modelUuid
	);
}