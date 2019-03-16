package com.easyapper.eventsmicroservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.easyapper.eventsmicroservice.exception.InvalidImageFileNameException;
import com.easyapper.eventsmicroservice.utility.EAUtil;

@Service
public class ImageService {
	
	public byte[] getImage(String imageName) throws IOException, InvalidImageFileNameException {
		Path imagePath = Paths.get(EAUtil.getImageRootDir(), imageName);
		if(!imagePath.toFile().exists()) {
			throw new InvalidImageFileNameException();
		}
		return Files.readAllBytes(imagePath);
	}
}
