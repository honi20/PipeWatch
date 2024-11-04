package com.pipewatch.domain.pipelineModel.service;

import com.pipewatch.domain.pipelineModel.model.dto.PipelineModelResponse;
import com.pipewatch.domain.pipelineModel.model.entity.PipelineModel;
import com.pipewatch.domain.pipelineModel.repository.PipelineModelRepository;
import com.pipewatch.domain.user.model.entity.Role;
import com.pipewatch.domain.user.model.entity.User;
import com.pipewatch.domain.user.repository.UserRepository;
import com.pipewatch.global.exception.BaseException;
import com.pipewatch.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.pipewatch.global.statusCode.ErrorCode.FORBIDDEN_USER_ROLE;
import static com.pipewatch.global.statusCode.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PipelineModelServiceImpl implements PipelineModelService{
	private final UserRepository userRepository;
	private final PipelineModelRepository pipelineModelRepository;
	private final S3Service s3Service;

	@Override
	@Transactional
	public PipelineModelResponse.FileUploadDto uploadFile(Long userId, MultipartFile file) throws IOException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new BaseException(USER_NOT_FOUND));

		// 관리자 및 기업만 가능
		if (user.getRole() == Role.USER || user.getRole() == Role.EMPLOYEE) {
			throw new BaseException(FORBIDDEN_USER_ROLE);
		}

		String UUID = java.util.UUID.randomUUID().toString();

		PipelineModel pipelineModel = PipelineModel.builder()
				.user(user)
				.isCompleted(true)
				.uuid(UUID)
				.build();

		if (!file.isEmpty()) {
			String imgUrl = s3Service.upload(file, "pipeline/model", UUID);
			pipelineModel.updateModelingUrl(imgUrl);
		}

		pipelineModelRepository.save(pipelineModel);

		return PipelineModelResponse.FileUploadDto.builder()
				.modelId(pipelineModel.getId())
				.build();
	}
}
