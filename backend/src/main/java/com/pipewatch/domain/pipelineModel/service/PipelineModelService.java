package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PipelineModelService {
	PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile file) throws IOException;
}
