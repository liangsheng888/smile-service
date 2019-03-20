package org.flightythought.smile.admin.service.impl;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.flightythought.smile.admin.common.GlobalConstant;
import org.flightythought.smile.admin.database.entity.CourseImageEntity;
import org.flightythought.smile.admin.database.entity.CourseRegistrationEntity;
import org.flightythought.smile.admin.database.entity.SysParameterEntity;
import org.flightythought.smile.admin.database.entity.SysUserEntity;
import org.flightythought.smile.admin.database.repository.CourseImageRepository;
import org.flightythought.smile.admin.database.repository.CourseRegistrationRepository;
import org.flightythought.smile.admin.database.repository.SysParameterRepository;
import org.flightythought.smile.admin.framework.exception.FlightyThoughtException;
import org.flightythought.smile.admin.service.CourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    @Autowired
    private SysParameterRepository sysParameterRepository;
    @Autowired
    private CourseRegistrationRepository courseRegistrationRepository;
    @Autowired
    private CourseImageRepository courseImageRepository;

    private static final String COURSE_IMAGE_PATH = "courseimage";

    @Override
    public CourseRegistrationEntity addCourseRegistration(CourseRegistrationEntity courseRegistrationEntity, MultipartFile coverPicture, List<MultipartFile> images, HttpSession session) throws FlightyThoughtException {
        // 获取系统参数
        SysParameterEntity sysParameterEntity = sysParameterRepository.getFilePathParam();
        SysUserEntity sysUserEntity = (SysUserEntity) session.getAttribute(GlobalConstant.USER_SESSION);
        courseRegistrationEntity.setCreateUserName(sysUserEntity.getLoginName());
        String imagePath, userPath;
        if (sysParameterEntity == null) {
            throw new FlightyThoughtException("请设置上传文件路径系统参数");
        } else {
            imagePath = sysParameterEntity.getParameterValue();
            userPath = File.separator + COURSE_IMAGE_PATH + File.separator + sysUserEntity.getId();
            File file = new File(imagePath + userPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if (coverPicture != null) {
            // 上传封面图片到服务器
            // 获取原始图片名称
            String coverPictureName = coverPicture.getOriginalFilename();
            // 新建文件
            if (coverPictureName != null) {
                String path = userPath + File.separator + System.currentTimeMillis() + coverPictureName.substring(coverPictureName.lastIndexOf("."));
                // 封面图片名称
                courseRegistrationEntity.setCoverPictureName(coverPictureName);
                // 封面图片路径
                courseRegistrationEntity.setCoverPicturePath(path);
                File coverPictureFile = new File(imagePath + path);
                try {
                    // 创建文件输出流
                    FileOutputStream fileOutputStream = new FileOutputStream(coverPictureFile);
                    // 复制文件
                    IOUtils.copy(coverPicture.getInputStream(), fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // 保存课程
        CourseRegistrationEntity courseRegistration = courseRegistrationRepository.save(courseRegistrationEntity);
        if (courseRegistration != null && images.size() > 0) {
            int id = courseRegistration.getCourseId();
            for (MultipartFile image : images) {
                CourseImageEntity courseImageEntity = new CourseImageEntity();
                // 课程ID
                courseImageEntity.setCourseId(id);
                // 图片大小
                courseImageEntity.setSize((double) image.getSize());
                // 获取原始图片名称
                String imageName = image.getOriginalFilename();
                // 新建文件
                if (imageName != null) {
                    String path = userPath + File.separator + System.currentTimeMillis() + imageName.substring(imageName.lastIndexOf("."));
                    // 图片名称
                    courseImageEntity.setFileName(imageName);
                    // 图片路径
                    courseImageEntity.setPath(path);
                    File coverPictureFile = new File(imagePath + path);
                    try {
                        // 创建文件输出流
                        FileOutputStream fileOutputStream = new FileOutputStream(coverPictureFile);
                        // 复制文件
                        IOUtils.copy(image.getInputStream(), fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                courseImageEntity.setCreateUserName(sysUserEntity.getLoginName());
                courseImageRepository.save(courseImageEntity);
            }

        }
        return courseRegistration;
    }

    @Override
    public Page<CourseRegistrationEntity> getCourseRegistration(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        return courseRegistrationRepository.findAll(pageable);
    }
}