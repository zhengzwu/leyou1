package com.leyou.serviceimpl;

import com.leyou.service.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadServiceImpl implements UploadService {
    private static final List<String>CONTENT_TYPE = Arrays.asList("image/png", "image/bmp", "image/jpeg");
    @Override
    public String upload(MultipartFile file) {

        try {
            String contentType = file.getContentType();
            if (!CONTENT_TYPE.contains(contentType)) {
                System.out.println("文件后缀不匹配");
                return null;
            }

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                System.out.println("这不是图片");
                return null;
            }
            File dest = new File("F:\\leyou\\image", file.getOriginalFilename());
            file.transferTo(dest);
            String url = "F:\\leyou\\image"+file.getOriginalFilename();
            return url;
        } catch (Exception e) {
            return null;
        }
    }}
